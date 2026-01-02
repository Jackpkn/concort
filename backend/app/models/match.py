import enum
import uuid
from datetime import datetime

from app.core.database import Base
from sqlalchemy import Column, DateTime, ForeignKey
from sqlalchemy import Enum as SQLEnum
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship


class MatchStatus(str, enum.Enum):
    """Match status enum."""

    ACTIVE = "ACTIVE"
    COMPLETED = "COMPLETED"
    EXPIRED = "EXPIRED"
    CANCELLED = "CANCELLED"


class Match(Base):
    """Match model - represents a connection between two users."""

    __tablename__ = "matches"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)

    # Users in the match
    male_user_id = Column(UUID(as_uuid=True), ForeignKey("users.id"), nullable=False)
    female_user_id = Column(UUID(as_uuid=True), ForeignKey("users.id"), nullable=False)

    # Status
    status = Column(SQLEnum(MatchStatus), default=MatchStatus.ACTIVE)

    # Timestamps
    matched_at = Column(DateTime, default=datetime.utcnow)
    completed_at = Column(DateTime, nullable=True)

    # Relationships
    male_user = relationship(
        "User", foreign_keys=[male_user_id], back_populates="matches_as_male"
    )
    female_user = relationship(
        "User", foreign_keys=[female_user_id], back_populates="matches_as_female"
    )
    messages = relationship(
        "Message", back_populates="match", cascade="all, delete-orphan"
    )

    def __repr__(self):
        return f"<Match {self.id}>"
