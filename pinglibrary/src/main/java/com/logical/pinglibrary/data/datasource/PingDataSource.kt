package com.logical.pinglibrary.data.datasource

import com.logical.pinglibrary.data.model.PingResult

/**
 * Interface for ping operations
 */
interface PingDataSource {
    /**
     * Performs ping test on specified host
     *
     * @param host The hostname or IP address to ping
     * @param pingCount Number of pings to perform (range: 1-10)
     * @return Result containing PingResult with latency information
     */
    suspend fun pingHost(
        host: String,
        pingCount: Int = 5
    ): Result<PingResult>
}

