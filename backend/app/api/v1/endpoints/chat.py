"""
Chat endpoints:
- Get messages
- Send message
- Mark as read
"""

from uuid import UUID

from app.api.v1.endpoints.users import get_current_user
from app.core.database import get_db
from app.models.match import Match
from app.models.message import Message
from app.models.user import User
from app.schemas import ChatHistoryResponse, MessageCreate, MessageResponse
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select, update
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter(prefix="/chat", tags=["Chat"])


async def verify_match_access(
    match_id: UUID, current_user: User, db: AsyncSession
) -> Match:
    """Verify user has access to this match."""
    query = select(Match).where(Match.id == match_id)
    result = await db.execute(query)
    match = result.scalar_one_or_none()

    if not match:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Match not found"
        )

    if (
        match.male_user_id != current_user.id
        and match.female_user_id != current_user.id
    ):
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="You are not part of this match",
        )

    return match


@router.get("/{match_id}/messages", response_model=ChatHistoryResponse)
async def get_messages(
    match_id: UUID,
    limit: int = 50,
    offset: int = 0,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db),
):
    """Get chat messages for a match."""
    match = await verify_match_access(match_id, current_user, db)

    # Get messages
    query = (
        select(Message)
        .where(Message.match_id == match_id)
        .order_by(Message.sent_at.asc())
        .offset(offset)
        .limit(limit + 1)  # Get one extra to check if there's more
    )
    result = await db.execute(query)
    messages = list(result.scalars().all())

    has_more = len(messages) > limit
    if has_more:
        messages = messages[:limit]

    # Mark unread messages as read
    await db.execute(
        update(Message)
        .where(Message.match_id == match_id)
        .where(Message.sender_id != current_user.id)
        .where(Message.is_read == False)
        .values(is_read=True)
    )
    await db.commit()

    return ChatHistoryResponse(
        messages=[
            MessageResponse(
                id=msg.id,
                match_id=msg.match_id,
                sender_id=msg.sender_id,
                content=msg.content,
                is_read=msg.is_read,
                sent_at=msg.sent_at,
                is_sent_by_me=msg.sender_id == current_user.id,
            )
            for msg in messages
        ],
        total=len(messages),
        has_more=has_more,
    )


@router.post("/{match_id}/messages", response_model=MessageResponse)
async def send_message(
    match_id: UUID,
    request: MessageCreate,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db),
):
    """Send a message in a match chat."""
    match = await verify_match_access(match_id, current_user, db)

    # Create message
    message = Message(
        match_id=match_id, sender_id=current_user.id, content=request.content
    )
    db.add(message)
    await db.commit()
    await db.refresh(message)

    return MessageResponse(
        id=message.id,
        match_id=message.match_id,
        sender_id=message.sender_id,
        content=message.content,
        is_read=message.is_read,
        sent_at=message.sent_at,
        is_sent_by_me=True,
    )


@router.post("/{match_id}/read")
async def mark_messages_as_read(
    match_id: UUID,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db),
):
    """Mark all messages in a chat as read."""
    match = await verify_match_access(match_id, current_user, db)

    await db.execute(
        update(Message)
        .where(Message.match_id == match_id)
        .where(Message.sender_id != current_user.id)
        .where(Message.is_read == False)
        .values(is_read=True)
    )
    await db.commit()

    return {"message": "Messages marked as read"}
