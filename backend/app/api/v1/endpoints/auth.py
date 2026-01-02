"""
Authentication endpoints:
- Register (send OTP)
- Verify OTP
- Profile setup
"""

import random
from datetime import datetime, timedelta

from app.core.config import settings
from app.core.database import get_db
from app.core.matching_engine import MatchingEngine
from app.core.security import create_access_token
from app.models.user import Gender as ModelGender
from app.models.user import User, UserStatus
from app.schemas import (
    ProfileSetupRequest,
    RegisterRequest,
    RegisterResponse,
    TokenResponse,
    UserResponse,
    VerifyOtpRequest,
)
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter(prefix="/auth", tags=["Authentication"])


def generate_otp() -> str:
    """Generate a 6-digit OTP code."""
    if settings.DEV_MODE:
        return settings.DEV_OTP_CODE
    return str(random.randint(100000, 999999))


async def send_otp_sms(phone_number: str, otp: str) -> bool:
    """Send OTP via SMS. Returns True if sent successfully."""
    if settings.DEV_MODE:
        print(f"[DEV] OTP for {phone_number}: {otp}")
        return True

    # In production, use Twilio
    # TODO: Implement Twilio SMS sending
    return True


@router.post("/register", response_model=RegisterResponse)
async def register(request: RegisterRequest, db: AsyncSession = Depends(get_db)):
    """
    Register with phone number. Sends OTP for verification.
    In DEV mode, OTP is always 123456.
    """
    phone = request.phone_number.strip()

    # Check if user exists
    query = select(User).where(User.phone_number == phone)
    result = await db.execute(query)
    user = result.scalar_one_or_none()

    # Generate OTP
    otp = generate_otp()
    otp_expires = datetime.utcnow() + timedelta(minutes=10)

    if user:
        # Update existing user's OTP
        user.otp_code = otp
        user.otp_expires_at = otp_expires
    else:
        # Create new user
        user = User(
            phone_number=phone,
            otp_code=otp,
            otp_expires_at=otp_expires,
            status=UserStatus.PENDING_VERIFICATION,
        )
        db.add(user)

    await db.commit()

    # Send OTP SMS
    await send_otp_sms(phone, otp)

    return RegisterResponse(
        message="OTP sent successfully"
        if not settings.DEV_MODE
        else f"OTP sent (DEV: {otp})",
        phone_number=phone,
    )


@router.post("/verify-otp", response_model=TokenResponse)
async def verify_otp(request: VerifyOtpRequest, db: AsyncSession = Depends(get_db)):
    """
    Verify OTP and get access token.
    In DEV mode, OTP 123456 always works.
    """
    phone = request.phone_number.strip()

    # Find user
    query = select(User).where(User.phone_number == phone)
    result = await db.execute(query)
    user = result.scalar_one_or_none()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found. Please register first.",
        )

    # Verify OTP
    if not user.otp_code or user.otp_code != request.otp_code:
        # In dev mode, also accept the dev OTP
        if not (settings.DEV_MODE and request.otp_code == settings.DEV_OTP_CODE):
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST, detail="Invalid OTP code"
            )

    # Check expiration
    if user.otp_expires_at and user.otp_expires_at < datetime.utcnow():
        if not settings.DEV_MODE:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="OTP has expired. Please request a new one.",
            )

    # Mark as verified
    user.is_verified = True
    user.otp_code = None
    user.otp_expires_at = None

    await db.commit()

    # Generate token
    token = create_access_token(subject=str(user.id))

    # Check if profile is complete
    is_profile_complete = bool(user.name and user.gender)

    return TokenResponse(
        access_token=token,
        token_type="bearer",
        user_id=user.id,
        is_profile_complete=is_profile_complete,
    )


@router.post("/profile-setup", response_model=UserResponse)
async def setup_profile(
    request: ProfileSetupRequest,
    db: AsyncSession = Depends(get_db),
    user_id: str = None,  # TODO: Get from JWT token
):
    """
    Complete profile setup and join the queue.
    """
    # For now, use a simple user lookup (in production, use JWT)
    # This will be improved with proper auth middleware

    # Get the most recently verified user without a profile
    query = (
        select(User)
        .where(User.is_verified == True)
        .where(User.name == None)
        .order_by(User.registered_at.desc())
        .limit(1)
    )
    result = await db.execute(query)
    user = result.scalar_one_or_none()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No user found pending profile setup",
        )

    # Update profile
    user.name = request.name
    user.gender = ModelGender(request.gender.value)
    user.age = request.age
    user.city = request.city

    # Add to queue
    engine = MatchingEngine(db)
    await engine.add_to_queue(user)

    await db.commit()
    await db.refresh(user)

    return UserResponse.model_validate(user)


@router.post("/resend-otp", response_model=RegisterResponse)
async def resend_otp(request: RegisterRequest, db: AsyncSession = Depends(get_db)):
    """Resend OTP to phone number."""
    return await register(request, db)
