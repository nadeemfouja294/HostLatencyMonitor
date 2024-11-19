package com.logical.hostlatencymonitor.presentation.model

sealed class PingState {
    data object Loading : PingState()
    data class Success(val results: List<PresentationPingResult>) : PingState()
    data class Error(val message: String) : PingState()
}