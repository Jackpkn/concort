import uuid
from datetime import datetime

from app.core.database import Base
from sqlalchemy import Boolean, Column, DateTime, ForeignKey, Text
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship


class Message(Base):
    """Chat message model."""

    __tablename__ = "messages"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)

    # Match relationship
    match_id = Column(UUID(as_uuid=True), ForeignKey("matches.id"), nullable=False)

    # Sender
    sender_id = Column(UUID(as_uuid=True), ForeignKey("users.id"), nullable=False)

    # Message content
    content = Column(Text, nullable=False)

    # Status
    is_read = Column(Boolean, default=False)

    # Timestamps
    sent_at = Column(DateTime, default=datetime.utcnow)

    # Relationships
    match = relationship("Match", back_populates="messages")
    sender = relationship("User")

    def __repr__(self):
        return f"<Message {self.id}>"
