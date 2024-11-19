package com.logical.pinglibrary.data.repository

import com.logical.pinglibrary.data.datasource.PingDataSource
import com.logical.pinglibrary.data.mapper.toDomainModel
import com.logical.pinglibrary.domain.model.PingResult as DomainPingResult
import com.logical.pinglibrary.domain.repository.PingRepository

class PingRepositoryImpl(private val dataSource: PingDataSource) : PingRepository {
    override suspend fun pingHost(host: String, pingCount: Int): DomainPingResult {
            val dataPingResult = dataSource.pingHost(host, pingCount).getOrThrow()
            return dataPingResult.toDomainModel()
    }
}
