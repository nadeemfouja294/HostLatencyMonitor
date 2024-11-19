package com.logical.pinglibrary.data.model

data class PingResult(
    val host: String,
    val averageLatency: Double?,
    val isReachable: Boolean
)
