package com.logical.hostlatencymonitor.domain.usecase

import com.logical.hostlatencymonitor.presentation.model.PresentationPingResult
import com.logical.pinglibrary.domain.model.PingResult
import com.logical.pinglibrary.domain.usecase.PingHostUseCase

class PingHostUseCaseWrapper(private val pingHostUseCase: PingHostUseCase) {
    suspend operator fun invoke(host: String, pingCount: Int): Result<PingResult> {
        return try {
            val domainPingResult = pingHostUseCase(host,pingCount )
            Result.success(domainPingResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}