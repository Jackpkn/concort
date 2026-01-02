from datetime import datetime
from enum import Enum
from typing import List, Optional
from uuid import UUID

from app.schemas.user import UserPublic
from pydantic import BaseModel


class MatchStatus(str, Enum):
    ACTIVE = "ACTIVE"
    COMPLETED = "COMPLETED"
    EXPIRED = "EXPIRED"
    CANCELLED = "CANCELLED"


# ===== Match Schemas =====


class MatchResponse(BaseModel):
    """Match response with partner info."""

    id: UUID
    partner: UserPublic
    status: MatchStatus
    matched_at: datetime
    unread_count: int = 0
    last_message: Optional[str] = None
    last_message_at: Optional[datetime] = None

    class Config:
        from_attributes = True


class MatchListResponse(BaseModel):
    """List of matches."""

    matches: List[MatchResponse]
    total: int


# ===== Message Schemas =====


class MessageCreate(BaseModel):
    """Request to send a message."""

    content: str


class MessageResponse(BaseModel):
    """Message response."""

    id: UUID
    match_id: UUID
    sender_id: UUID
    content: str
    is_read: bool
    sent_at: datetime
    is_sent_by_me: bool = False

    class Config:
        from_attributes = True


class ChatHistoryResponse(BaseModel):
    """Chat history response."""

    messages: List[MessageResponse]
    total: int
    has_more: bool


class MatchEventResponse(BaseModel):
    """Response when a match happens."""

    message: str
    match_id: UUID
    partner: UserPublic
