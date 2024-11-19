package com.logical.hostlatencymonitor.data.repository

import com.logical.hostlatencymonitor.data.datasource.HostDataSource
import com.logical.hostlatencymonitor.data.mapper.toDomainModel
import com.logical.hostlatencymonitor.domain.model.Host
import com.logical.hostlatencymonitor.domain.repository.HostRepository
import javax.inject.Inject

class HostRepositoryImpl @Inject constructor(
    private val dataSource: HostDataSource
) : HostRepository {
    override suspend fun fetchHosts(jsonUrl: String): Result<List<Host>> {
        return dataSource.fetchHosts(jsonUrl).map { dataModels ->
            dataModels.map { it.toDomainModel() }
        }
    }
}
