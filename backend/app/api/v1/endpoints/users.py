"""
User endpoints:
- Get current user
- Get queue status
- Update profile
"""

from uuid import UUID

from app.core.database import get_db
from app.core.matching_engine import MatchingEngine
from app.core.security import decode_token
from app.models.user import User, UserStatus
from app.schemas import QueueStatusResponse, UserResponse
from fastapi import APIRouter, Depends, Header, HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter(prefix="/users", tags=["Users"])


async def get_current_user(
    authorization: str = Header(None), db: AsyncSession = Depends(get_db)
) -> User:
    """Dependency to get current authenticated user."""
    if not authorization:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Authorization header missing",
        )

    # Extract token from "Bearer <token>"
    try:
        scheme, token = authorization.split()
        if scheme.lower() != "bearer":
            raise ValueError("Invalid scheme")
    except ValueError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authorization header format",
        )

    # Decode token
    user_id = decode_token(token)
    if not user_id:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid or expired token"
        )

    # Get user from database
    query = select(User).where(User.id == UUID(user_id))
    result = await db.execute(query)
    user = result.scalar_one_or_none()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="User not found"
        )

    return user


@router.get("/me", response_model=UserResponse)
async def get_me(current_user: User = Depends(get_current_user)):
    """Get current user's profile."""
    return UserResponse.model_validate(current_user)


@router.get("/queue-status", response_model=QueueStatusResponse)
async def get_queue_status(
    current_user: User = Depends(get_current_user), db: AsyncSession = Depends(get_db)
):
    """Get current queue status and user's rank."""
    if current_user.status != UserStatus.WAITING:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="You are not in the waiting queue",
        )

    engine = MatchingEngine(db)
    stats = await engine.get_queue_stats()

    return QueueStatusResponse(
        rank=current_user.queue_rank or 0,
        estimated_wait_time=None,  # Could calculate based on match rate
        males_waiting=stats["males_waiting"],
        females_waiting=stats["females_waiting"],
        last_updated=stats["last_updated"],
    )


@router.get("/{user_id}", response_model=UserResponse)
async def get_user(
    user_id: UUID,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db),
):
    """Get a user by ID (for viewing match profiles)."""
    query = select(User).where(User.id == user_id)
    result = await db.execute(query)
    user = result.scalar_one_or_none()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="User not found"
        )

    return UserResponse.model_validate(user)
