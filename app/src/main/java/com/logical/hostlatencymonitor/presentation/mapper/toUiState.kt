package com.logical.hostlatencymonitor.presentation.mapper

import com.logical.hostlatencymonitor.domain.model.Host
import com.logical.pinglibrary.domain.model.PingResult
import com.logical.hostlatencymonitor.presentation.model.HostUiState

fun Host.toUiState(pingResult: PingResult): HostUiState.Success {
    return HostUiState.Success(
        name = this.name,
        latency = pingResult.averageLatency?.toString() ?: "N/A",
        reachable = pingResult.isReachable,
        iconUrl = this.icon
    )
}
