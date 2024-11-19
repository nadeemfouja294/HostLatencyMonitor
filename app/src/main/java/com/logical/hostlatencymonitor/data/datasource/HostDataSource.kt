package com.logical.hostlatencymonitor.data.datasource

import com.logical.hostlatencymonitor.data.model.HostDataModel

interface HostDataSource {
    suspend fun fetchHosts(jsonUrl: String): Result<List<HostDataModel>>
}
