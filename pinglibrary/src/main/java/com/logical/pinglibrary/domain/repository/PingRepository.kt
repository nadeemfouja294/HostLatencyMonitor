package com.logical.pinglibrary.domain.repository

import com.logical.pinglibrary.domain.model.PingResult

interface PingRepository {
    suspend fun pingHost(host: String, pingCount: Int = 5): PingResult
}
