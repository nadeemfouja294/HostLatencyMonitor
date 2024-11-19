package com.logical.hostlatencymonitor.presentation.model

sealed class HostUiState {
    data object Loading : HostUiState()

    data class Success(
        val name: String,
        val latency: String,
        val reachable: Boolean,
        val iconUrl: String
    ) : HostUiState()

    data class Failure(
        val errorMessage: String
    ) : HostUiState()
}
