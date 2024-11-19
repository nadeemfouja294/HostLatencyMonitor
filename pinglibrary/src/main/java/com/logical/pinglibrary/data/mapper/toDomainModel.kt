package com.logical.pinglibrary.data.mapper

import com.logical.pinglibrary.data.model.PingResult as DataPingResult
import com.logical.pinglibrary.domain.model.PingResult as DomainPingResult

fun DataPingResult.toDomainModel(): DomainPingResult {
    return DomainPingResult(
        host = this.host,
        averageLatency = this.averageLatency,
        isReachable = this.isReachable
    )
}
