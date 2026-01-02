package com.example.concort.data.repository

import com.example.concort.data.api.ConcortApi
import com.example.concort.data.api.model.*
import com.example.concort.data.local.TokenManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val api: ConcortApi,
    private val tokenManager: TokenManager
) {
    
    suspend fun getMatches(): Result<MatchListResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.getMatches(token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun getMatch(matchId: String): Result<MatchResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.getMatch(token, matchId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}

@Singleton
class ChatRepository @Inject constructor(
    private val api: ConcortApi,
    private val tokenManager: TokenManager
) {
    
    suspend fun getMessages(matchId: String, limit: Int = 50, offset: Int = 0): Result<ChatHistoryResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.getMessages(token, matchId, limit, offset)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun sendMessage(matchId: String, content: String): Result<MessageResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.sendMessage(token, matchId, MessageRequest(content))
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun markAsRead(matchId: String): Result<Unit> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.markMessagesAsRead(token, matchId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
}
