package com.example.concort.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concort.data.api.model.MessageResponse
import com.example.concort.data.api.model.MatchResponse
import com.example.concort.data.repository.ChatRepository
import com.example.concort.data.repository.MatchRepository
import com.example.concort.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val match: MatchResponse? = null,
    val messages: List<MessageResponse> = emptyList(),
    val isSending: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val matchRepository: MatchRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val matchId: String = savedStateHandle.get<String>("matchId") ?: ""
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    init {
        if (matchId.isNotEmpty()) {
            loadMatch()
            loadMessages()
        }
    }
    
    private fun loadMatch() {
        viewModelScope.launch {
            when (val result = matchRepository.getMatch(matchId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(match = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
            }
        }
    }
    
    fun loadMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = chatRepository.getMessages(matchId)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(isLoading = false, messages = result.data.messages) 
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
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            
            when (val result = chatRepository.sendMessage(matchId, content)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isSending = false, 
                            messages = state.messages + result.data
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(isSending = false, errorMessage = result.message) 
                    }
                }
            }
        }
    }
    
    fun markAsRead() {
        viewModelScope.launch {
            chatRepository.markAsRead(matchId)
        }
    }
}
