package com.logical.pinglibrary.domain.usecase

import com.logical.pinglibrary.domain.model.PingResult
import com.logical.pinglibrary.domain.repository.PingRepository

class PingHostUseCase(private val repository: PingRepository) {
    suspend operator fun invoke(host: String, pingCount: Int): PingResult {
        return repository.pingHost(host, pingCount)
    }
}
