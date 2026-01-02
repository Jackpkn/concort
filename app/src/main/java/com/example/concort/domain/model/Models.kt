package com.example.concort.domain.model

import java.util.UUID

/**
 * User domain model
 */
data class User(
    val id: String = UUID.randomUUID().toString(),
    val phoneNumber: String,
    val name: String,
    val gender: Gender,
    val age: Int? = null,
    val city: String? = null,
    val isVerified: Boolean = false,
    val status: UserStatus = UserStatus.PENDING_VERIFICATION,
    val queueRank: Int? = null,
    val profileImageUrl: String? = null
)

/**
 * Match domain model - represents a connection between two users
 */
data class Match(
    val id: String = UUID.randomUUID().toString(),
    val partnerId: String,
    val partnerName: String,
    val partnerAge: Int? = null,
    val partnerCity: String? = null,
    val partnerImageUrl: String? = null,
    val matchedAt: Long = System.currentTimeMillis(),
    val status: MatchStatus = MatchStatus.ACTIVE,
    val lastMessage: String? = null,
    val unreadCount: Int = 0
)

/**
 * Chat message model
 */
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val matchId: String,
    val senderId: String,
    val content: String,
    val sentAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isSentByMe: Boolean = false
)

/**
 * Queue status for waiting users
 */
data class QueueStatus(
    val rank: Int,
    val estimatedWaitTime: String? = null,
    val malesWaiting: Int,
    val femalesWaiting: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
