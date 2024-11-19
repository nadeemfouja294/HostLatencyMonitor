package com.logical.hostlatencymonitor.domain.repository

import com.logical.hostlatencymonitor.domain.model.Host

interface HostRepository {
    suspend fun fetchHosts(jsonUrl: String): Result<List<Host>>
}
