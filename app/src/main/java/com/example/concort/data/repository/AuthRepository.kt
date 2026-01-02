package com.example.concort.data.repository

import com.example.concort.data.api.ConcortApi
import com.example.concort.data.api.model.*
import com.example.concort.data.local.TokenManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
}

@Singleton
class AuthRepository @Inject constructor(
    private val api: ConcortApi,
    private val tokenManager: TokenManager
) {
    
    suspend fun register(phoneNumber: String): Result<RegisterResponse> {
        return try {
            val response = api.register(RegisterRequest(phoneNumber))
            if (response.isSuccessful && response.body() != null) {
                tokenManager.savePhoneNumber(phoneNumber)
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun verifyOtp(phoneNumber: String, otpCode: String): Result<TokenResponse> {
        return try {
            val response = api.verifyOtp(VerifyOtpRequest(phoneNumber, otpCode))
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                tokenManager.saveToken(data.accessToken, data.userId)
                Result.Success(data)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun setupProfile(
        name: String,
        gender: String,
        age: Int?,
        city: String?
    ): Result<UserResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.setupProfile(token, ProfileSetupRequest(name, gender, age, city))
            if (response.isSuccessful && response.body() != null) {
                tokenManager.saveUserName(name)
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun getCurrentUser(): Result<UserResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.getCurrentUser(token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun getQueueStatus(): Result<QueueStatusResponse> {
        return try {
            val token = tokenManager.getAuthHeader().first() ?: return Result.Error("Not authenticated")
            val response = api.getQueueStatus(token)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message(), response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    
    suspend fun logout() {
        tokenManager.clearAll()
    }
    
    fun isLoggedIn() = tokenManager.accessToken
    fun getUserName() = tokenManager.userName
}
