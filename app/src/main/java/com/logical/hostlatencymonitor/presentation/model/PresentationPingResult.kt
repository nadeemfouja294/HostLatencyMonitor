package com.logical.hostlatencymonitor.presentation.model

data class PresentationPingResult(
    val name: String,
    val hostUrl: String,
    val iconUrl: String?=null,
    val averageLatency: Float?,
    val success: Boolean,
    val isLoading: Boolean = false
)