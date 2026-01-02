package com.example.concort.data.api

import com.example.concort.data.api.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Concort API Service - Retrofit interface for backend communication
 */
interface ConcortApi {
    
    // ===== Auth Endpoints =====
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    
    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<TokenResponse>
    
    @POST("auth/profile-setup")
    suspend fun setupProfile(
        @Header("Authorization") token: String,
        @Body request: ProfileSetupRequest
    ): Response<UserResponse>
    
    @POST("auth/resend-otp")
    suspend fun resendOtp(@Body request: RegisterRequest): Response<RegisterResponse>
    
    // ===== User Endpoints =====
    
    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserResponse>
    
    @GET("users/queue-status")
    suspend fun getQueueStatus(@Header("Authorization") token: String): Response<QueueStatusResponse>
    
    // ===== Match Endpoints =====
    
    @GET("matches")
    suspend fun getMatches(@Header("Authorization") token: String): Response<MatchListResponse>
    
    @GET("matches/{matchId}")
    suspend fun getMatch(
        @Header("Authorization") token: String,
        @Path("matchId") matchId: String
    ): Response<MatchResponse>
    
    // ===== Chat Endpoints =====
    
    @GET("chat/{matchId}/messages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Path("matchId") matchId: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<ChatHistoryResponse>
    
    @POST("chat/{matchId}/messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Path("matchId") matchId: String,
        @Body request: MessageRequest
    ): Response<MessageResponse>
    
    @POST("chat/{matchId}/read")
    suspend fun markMessagesAsRead(
        @Header("Authorization") token: String,
        @Path("matchId") matchId: String
    ): Response<Unit>
}
