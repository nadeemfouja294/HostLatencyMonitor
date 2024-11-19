package com.logical.pinglibrary.data.datasource

import com.logical.pinglibrary.data.model.PingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.math.round

/**
 * Implementation of a Ping service for measuring host latency
 */
class PingDataSourceImpl @Inject constructor() : PingDataSource {

    companion object {
        private const val MIN_PING_COUNT = 1
        private const val MAX_PING_COUNT = 10
    }

    override suspend fun pingHost(
        host: String,
        pingCount: Int
    ): Result<PingResult> = withContext(Dispatchers.IO) {
        // Validate ping count
        if (pingCount !in MIN_PING_COUNT..MAX_PING_COUNT) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Ping count must be between $MIN_PING_COUNT and $MAX_PING_COUNT"
                )
            )
        }

        try {
            val process = Runtime.getRuntime().exec("ping -c $pingCount $host")
            var totalLatency = 0.0
            var successfulPings = 0

            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.lineSequence()
                    .filter { it.contains("time=") }
                    .forEach { line ->
                        val time = line.substringAfter("time=")
                            .substringBefore(" ms")
                            .toDoubleOrNull()
                        if (time != null) {
                            totalLatency += time
                            successfulPings++
                        }
                    }
            }

            val averageLatency = if (successfulPings > 0) {
                (totalLatency / successfulPings).toBigDecimal()
                    .setScale(4, java.math.RoundingMode.FLOOR)
                    .toDouble()
            } else {
                null
            }
            Result.success(
                PingResult(
                    host = host,
                    averageLatency = averageLatency,
                    isReachable = successfulPings > 0
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
