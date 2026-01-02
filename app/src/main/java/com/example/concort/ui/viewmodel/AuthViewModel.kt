package com.example.concort.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concort.data.repository.AuthRepository
import com.example.concort.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOtpSent: Boolean = false,
    val isVerified: Boolean = false,
    val isProfileComplete: Boolean = false,
    val phoneNumber: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun register(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = authRepository.register(phoneNumber)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isOtpSent = true,
                            phoneNumber = phoneNumber
                        ) 
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = result.message) 
                    }
                }
            }
        }
    }
    
    fun verifyOtp(phoneNumber: String, otpCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = authRepository.verifyOtp(phoneNumber, otpCode)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            isVerified = true,
                            isProfileComplete = result.data.isProfileComplete
                        ) 
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = result.message) 
                    }
                }
            }
        }
    }
    
    fun setupProfile(name: String, gender: String, age: Int?, city: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = authRepository.setupProfile(name, gender, age, city)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(isLoading = false, isProfileComplete = true) 
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = result.message) 
                    }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun resetState() {
        _uiState.value = AuthUiState()
    }
}
