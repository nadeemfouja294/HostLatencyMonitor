package com.logical.hostlatencymonitor.domain.usecase

import com.logical.hostlatencymonitor.domain.model.Host
import com.logical.hostlatencymonitor.domain.repository.HostRepository

class FetchHostsUseCase(private val repository: HostRepository) {
    suspend operator fun invoke(jsonUrl: String): Result<List<Host>> {
        return repository.fetchHosts(jsonUrl)
    }
}
