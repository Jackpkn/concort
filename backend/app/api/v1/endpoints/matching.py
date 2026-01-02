"""
Matching endpoints:
- Get all matches
- Get match details
- Trigger queue processing (admin)
"""

from uuid import UUID

from app.api.v1.endpoints.users import get_current_user
from app.core.database import get_db
from app.core.matching_engine import MatchingEngine
from app.models.match import Match, MatchStatus
from app.models.message import Message
from app.models.user import User
from app.schemas import MatchListResponse, MatchResponse, UserPublic
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import func, or_, select
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

router = APIRouter(prefix="/matches", tags=["Matches"])


@router.get("", response_model=MatchListResponse)
async def get_matches(
    current_user: User = Depends(get_current_user), db: AsyncSession = Depends(get_db)
):
    """Get all matches for current user."""
    # Find all matches where user is either male or female
    query = (
        select(Match)
        .options(selectinload(Match.male_user), selectinload(Match.female_user))
        .where(
            or_(
                Match.male_user_id == current_user.id,
                Match.female_user_id == current_user.id,
            )
        )
        .order_by(Match.matched_at.desc())
    )
    result = await db.execute(query)
    matches = result.scalars().all()

    match_responses = []
    for match in matches:
        # Determine partner
        if match.male_user_id == current_user.id:
            partner = match.female_user
        else:
            partner = match.male_user

        # Get unread count
        unread_query = (
            select(func.count(Message.id))
            .where(Message.match_id == match.id)
            .where(Message.sender_id != current_user.id)
            .where(Message.is_read == False)
        )
        unread_result = await db.execute(unread_query)
        unread_count = unread_result.scalar() or 0

        # Get last message
        last_msg_query = (
            select(Message)
            .where(Message.match_id == match.id)
            .order_by(Message.sent_at.desc())
            .limit(1)
        )
        last_msg_result = await db.execute(last_msg_query)
        last_msg = last_msg_result.scalar_one_or_none()

        match_responses.append(
            MatchResponse(
                id=match.id,
                partner=UserPublic.model_validate(partner),
                status=MatchStatus(match.status.value),
                matched_at=match.matched_at,
                unread_count=unread_count,
                last_message=last_msg.content if last_msg else None,
                last_message_at=last_msg.sent_at if last_msg else None,
            )
        )

    return MatchListResponse(matches=match_responses, total=len(match_responses))


@router.get("/{match_id}", response_model=MatchResponse)
async def get_match(
    match_id: UUID,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db),
):
    """Get a specific match."""
    query = (
        select(Match)
        .options(selectinload(Match.male_user), selectinload(Match.female_user))
        .where(Match.id == match_id)
    )
    result = await db.execute(query)
    match = result.scalar_one_or_none()

    if not match:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Match not found"
        )

    # Verify user is part of this match
    if (
        match.male_user_id != current_user.id
        and match.female_user_id != current_user.id
    ):
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You are not part of this match",
        )

    # Determine partner
    if match.male_user_id == current_user.id:
        partner = match.female_user
    else:
        partner = match.male_user

    return MatchResponse(
        id=match.id,
        partner=UserPublic.model_validate(partner),
        status=MatchStatus(match.status.value),
        matched_at=match.matched_at,
        unread_count=0,
        last_message=None,
        last_message_at=None,
    )


@router.post("/process-queue")
async def process_queue(db: AsyncSession = Depends(get_db)):
    """
    Admin endpoint to manually trigger queue processing.
    In production, this would be protected with admin auth.
    """
    engine = MatchingEngine(db)
    new_matches = await engine.process_queue()

    return {
        "message": f"Processed queue. Created {len(new_matches)} new matches.",
        "new_matches": len(new_matches),
    }
