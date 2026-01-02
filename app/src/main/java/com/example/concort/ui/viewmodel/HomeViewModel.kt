package com.example.concort.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concort.data.api.model.QueueStatusResponse
import com.example.concort.data.api.model.MatchResponse
import com.example.concort.data.repository.AuthRepository
import com.example.concort.data.repository.MatchRepository
import com.example.concort.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userName: String = "User",
    val queueStatus: QueueStatusResponse? = null,
    val matches: List<MatchResponse> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Get user name
            authRepository.getUserName().collect { name ->
                _uiState.update { it.copy(userName = name ?: "User") }
            }
        }
        
        loadQueueStatus()
        loadMatches()
    }
    
    fun loadQueueStatus() {
        viewModelScope.launch {
            when (val result = authRepository.getQueueStatus()) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(isLoading = false, queueStatus = result.data) 
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
    
    fun loadMatches() {
        viewModelScope.launch {
            when (val result = matchRepository.getMatches()) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(matches = result.data.matches) 
                    }
                }
                is Result.Error -> {
                    // Silently fail for matches - user might just be in queue
                }
            }
        }
    }
    
    fun refresh() {
        loadQueueStatus()
        loadMatches()
    }
}
