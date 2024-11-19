package com.logical.hostlatencymonitor.data.datasource

import com.google.gson.Gson
import com.logical.hostlatencymonitor.data.model.HostDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class HostDataSourceImpl @Inject constructor(
    private val gson: Gson
) : HostDataSource {
    override suspend fun fetchHosts(jsonUrl: String): Result<List<HostDataModel>> {
        return try {
            withContext(Dispatchers.IO) {
                val json = URL(jsonUrl).readText()
                val hosts = gson.fromJson(json, Array<HostDataModel>::class.java).toList()
                Result.success(hosts)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
