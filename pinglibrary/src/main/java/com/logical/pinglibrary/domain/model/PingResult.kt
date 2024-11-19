package com.logical.pinglibrary.domain.model

data class PingResult(
    val host: String,
    val averageLatency: Double?,
    val isReachable: Boolean
)
