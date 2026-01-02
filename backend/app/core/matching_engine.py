"""
Concort Matching Engine

Core algorithm for fair, queue-based matching.
Rules:
1. First-come, first-match based on verified_at timestamp
2. Match only between MALE and FEMALE users in WAITING status
3. Update queue ranks for remaining users after matching
"""

from datetime import datetime
from typing import List, Optional
from uuid import UUID

from app.models.match import Match, MatchStatus
from app.models.user import Gender, User, UserStatus
from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession


class MatchingEngine:
    """Core matching engine for the dating platform."""

    def __init__(self, db: AsyncSession):
        self.db = db

    async def process_queue(self) -> List[Match]:
        """
        Process the queue and create matches.
        Called when:
        1. A new user gets verified
        2. Periodically via background job

        Returns list of new matches created.
        """
        new_matches = []

        # Get waiting males ordered by verified_at
        male_query = (
            select(User)
            .where(User.gender == Gender.MALE)
            .where(User.status == UserStatus.WAITING)
            .order_by(User.verified_at.asc())
        )
        male_result = await self.db.execute(male_query)
        males = list(male_result.scalars().all())

        # Get waiting females ordered by verified_at
        female_query = (
            select(User)
            .where(User.gender == Gender.FEMALE)
            .where(User.status == UserStatus.WAITING)
            .order_by(User.verified_at.asc())
        )
        female_result = await self.db.execute(female_query)
        females = list(female_result.scalars().all())

        # Match pairs in order
        match_count = min(len(males), len(females))

        for i in range(match_count):
            male = males[i]
            female = females[i]

            # Create the match
            match = Match(
                male_user_id=male.id,
                female_user_id=female.id,
                status=MatchStatus.ACTIVE,
            )
            self.db.add(match)

            # Update user statuses
            male.status = UserStatus.MATCHED
            male.queue_rank = None
            female.status = UserStatus.MATCHED
            female.queue_rank = None

            new_matches.append(match)

        # Update ranks for remaining users
        remaining_males = males[match_count:]
        remaining_females = females[match_count:]

        for rank, user in enumerate(remaining_males, start=1):
            user.queue_rank = rank

        for rank, user in enumerate(remaining_females, start=1):
            user.queue_rank = rank

        await self.db.commit()

        return new_matches

    async def get_queue_stats(self) -> dict:
        """Get current queue statistics."""
        # Count waiting males
        male_count_query = (
            select(func.count(User.id))
            .where(User.gender == Gender.MALE)
            .where(User.status == UserStatus.WAITING)
        )
        male_result = await self.db.execute(male_count_query)
        males_waiting = male_result.scalar() or 0

        # Count waiting females
        female_count_query = (
            select(func.count(User.id))
            .where(User.gender == Gender.FEMALE)
            .where(User.status == UserStatus.WAITING)
        )
        female_result = await self.db.execute(female_count_query)
        females_waiting = female_result.scalar() or 0

        return {
            "males_waiting": males_waiting,
            "females_waiting": females_waiting,
            "last_updated": datetime.utcnow(),
        }

    async def get_user_rank(self, user_id: UUID) -> Optional[int]:
        """Get user's current rank in queue."""
        query = select(User.queue_rank).where(User.id == user_id)
        result = await self.db.execute(query)
        return result.scalar()

    async def add_to_queue(self, user: User) -> int:
        """
        Add a verified user to the waiting queue.
        Returns their assigned rank.
        """
        # Get current max rank for this gender
        max_rank_query = (
            select(func.max(User.queue_rank))
            .where(User.gender == user.gender)
            .where(User.status == UserStatus.WAITING)
        )
        result = await self.db.execute(max_rank_query)
        max_rank = result.scalar() or 0

        # Assign next rank
        new_rank = max_rank + 1
        user.queue_rank = new_rank
        user.status = UserStatus.WAITING
        user.verified_at = datetime.utcnow()

        await self.db.commit()

        # Try to process queue (might create a match immediately)
        await self.process_queue()

        # Return updated rank (might be None if matched)
        await self.db.refresh(user)
        return user.queue_rank


async def get_matching_engine(db: AsyncSession) -> MatchingEngine:
    """Dependency for getting matching engine instance."""
    return MatchingEngine(db)
