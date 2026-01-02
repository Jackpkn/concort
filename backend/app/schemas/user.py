from datetime import datetime
from enum import Enum
from typing import Optional
from uuid import UUID

from pydantic import BaseModel, Field


class Gender(str, Enum):
    MALE = "MALE"
    FEMALE = "FEMALE"


class UserStatus(str, Enum):
    PENDING_VERIFICATION = "PENDING_VERIFICATION"
    WAITING = "WAITING"
    MATCHED = "MATCHED"
    INACTIVE = "INACTIVE"


# ===== Auth Schemas =====


class RegisterRequest(BaseModel):
    """Request to register with phone number."""

    phone_number: str = Field(
        ..., min_length=10, max_length=15, description="Phone number with country code"
    )


class RegisterResponse(BaseModel):
    """Response after registration - OTP sent."""

    message: str
    phone_number: str


class VerifyOtpRequest(BaseModel):
    """Request to verify OTP."""

    phone_number: str
    otp_code: str = Field(..., min_length=6, max_length=6)


class TokenResponse(BaseModel):
    """JWT token response."""

    access_token: str
    token_type: str = "bearer"
    user_id: UUID
    is_profile_complete: bool


class ProfileSetupRequest(BaseModel):
    """Request to complete profile."""

    name: str = Field(..., min_length=2, max_length=100)
    gender: Gender
    age: Optional[int] = Field(None, ge=18, le=100)
    city: Optional[str] = Field(None, max_length=100)


# ===== User Schemas =====


class UserBase(BaseModel):
    """Base user schema."""

    id: UUID
    phone_number: str
    name: Optional[str]
    gender: Optional[Gender]
    age: Optional[int]
    city: Optional[str]
    is_verified: bool
    status: UserStatus
    queue_rank: Optional[int]
    profile_image_url: Optional[str]
    registered_at: datetime

    class Config:
        from_attributes = True


class UserResponse(UserBase):
    """Full user response."""

    pass


class UserPublic(BaseModel):
    """Public user info (for matches)."""

    id: UUID
    name: Optional[str]
    age: Optional[int]
    city: Optional[str]
    profile_image_url: Optional[str]

    class Config:
        from_attributes = True


# ===== Queue Schemas =====


class QueueStatusResponse(BaseModel):
    """Queue status for waiting users."""

    rank: int
    estimated_wait_time: Optional[str]
    males_waiting: int
    females_waiting: int
    last_updated: datetime
