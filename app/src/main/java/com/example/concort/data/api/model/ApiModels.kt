package com.example.concort.data.api.model

import com.google.gson.annotations.SerializedName

// ===== Auth Models =====

data class RegisterRequest(
    @SerializedName("phone_number") val phoneNumber: String
)

data class RegisterResponse(
    val message: String,
    @SerializedName("phone_number") val phoneNumber: String
)

data class VerifyOtpRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("otp_code") val otpCode: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("is_profile_complete") val isProfileComplete: Boolean
)

data class ProfileSetupRequest(
    val name: String,
    val gender: String, // "MALE" or "FEMALE"
    val age: Int? = null,
    val city: String? = null
)

// ===== User Models =====

data class UserResponse(
    val id: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val name: String?,
    val gender: String?,
    val age: Int?,
    val city: String?,
    @SerializedName("is_verified") val isVerified: Boolean,
    val status: String,
    @SerializedName("queue_rank") val queueRank: Int?,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    @SerializedName("registered_at") val registeredAt: String
)

data class QueueStatusResponse(
    val rank: Int,
    @SerializedName("estimated_wait_time") val estimatedWaitTime: String?,
    @SerializedName("males_waiting") val malesWaiting: Int,
    @SerializedName("females_waiting") val femalesWaiting: Int,
    @SerializedName("last_updated") val lastUpdated: String
)

// ===== Match Models =====

data class MatchListResponse(
    val matches: List<MatchResponse>,
    val total: Int
)

data class MatchResponse(
    val id: String,
    val partner: UserPublic,
    val status: String,
    @SerializedName("matched_at") val matchedAt: String,
    @SerializedName("unread_count") val unreadCount: Int,
    @SerializedName("last_message") val lastMessage: String?,
    @SerializedName("last_message_at") val lastMessageAt: String?
)

data class UserPublic(
    val id: String,
    val name: String?,
    val age: Int?,
    val city: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?
)

// ===== Chat Models =====

data class MessageRequest(
    val content: String
)

data class MessageResponse(
    val id: String,
    @SerializedName("match_id") val matchId: String,
    @SerializedName("sender_id") val senderId: String,
    val content: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("sent_at") val sentAt: String,
    @SerializedName("is_sent_by_me") val isSentByMe: Boolean
)

data class ChatHistoryResponse(
    val messages: List<MessageResponse>,
    val total: Int,
    @SerializedName("has_more") val hasMore: Boolean
)
