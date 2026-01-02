import enum
import uuid
from datetime import datetime

from app.core.database import Base
from sqlalchemy import Boolean, Column, DateTime, Integer, String
from sqlalchemy import Enum as SQLEnum
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship


class Gender(str, enum.Enum):
    """User gender enum."""

    MALE = "MALE"
    FEMALE = "FEMALE"


class UserStatus(str, enum.Enum):
    """User status in the matching system."""

    PENDING_VERIFICATION = "PENDING_VERIFICATION"
    WAITING = "WAITING"
    MATCHED = "MATCHED"
    INACTIVE = "INACTIVE"


class User(Base):
    """User model for the dating platform."""

    __tablename__ = "users"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    phone_number = Column(String(20), unique=True, nullable=False, index=True)
    name = Column(String(100), nullable=True)
    gender = Column(SQLEnum(Gender), nullable=True)
    age = Column(Integer, nullable=True)
    city = Column(String(100), nullable=True)

    # Verification
    is_verified = Column(Boolean, default=False)
    otp_code = Column(String(6), nullable=True)
    otp_expires_at = Column(DateTime, nullable=True)

    # Queue/Ranking
    status = Column(SQLEnum(UserStatus), default=UserStatus.PENDING_VERIFICATION)
    queue_rank = Column(Integer, nullable=True)

    # Timestamps
    registered_at = Column(DateTime, default=datetime.utcnow)
    verified_at = Column(DateTime, nullable=True)
    last_active_at = Column(DateTime, default=datetime.utcnow)

    # Profile image
    profile_image_url = Column(String(500), nullable=True)

    # Relationships
    matches_as_male = relationship(
        "Match", foreign_keys="Match.male_user_id", back_populates="male_user"
    )
    matches_as_female = relationship(
        "Match", foreign_keys="Match.female_user_id", back_populates="female_user"
    )

    def __repr__(self):
        return f"<User {self.phone_number}>"
