package com.example.concort.domain.model

/**
 * Gender enum for user registration
 */
enum class Gender {
    MALE,
    FEMALE
}

/**
 * User status in the matching system
 */
enum class UserStatus {
    PENDING_VERIFICATION,  // Registered but not verified
    WAITING,               // Verified and in queue
    MATCHED,               // Has an active match
    INACTIVE               // Deactivated or left
}

/**
 * Match status
 */
enum class MatchStatus {
    ACTIVE,      // Currently chatting
    COMPLETED,   // Successfully completed (date happened)
    EXPIRED,     // Time limit exceeded without interaction
    CANCELLED    // One party cancelled
}
