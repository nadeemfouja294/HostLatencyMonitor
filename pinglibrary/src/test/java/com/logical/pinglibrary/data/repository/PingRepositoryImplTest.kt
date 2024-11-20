package com.logical.pinglibrary.data.repository

import com.logical.pinglibrary.data.datasource.PingDataSource
import com.logical.pinglibrary.domain.repository.PingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import com.logical.pinglibrary.data.model.PingResult as DataPingResult

class PingRepositoryImplTest {

    private val mockDataSource = mockk<PingDataSource>()

    private lateinit var pingRepository: PingRepository

    @Before
    fun setup() {
        pingRepository = PingRepositoryImpl(mockDataSource)
    }

    @Test
    fun `should return domain ping result when data source returns valid data`() = runTest {
        // Given
        val host = "www.google.com"
        val pingCount = 3
        val dataPingResult = DataPingResult(
            host = host,
            averageLatency = 15.5,
            isReachable = true
        )
        coEvery { mockDataSource.pingHost(host, pingCount) } returns Result.success(dataPingResult)

        // When
        val result = pingRepository.pingHost(host, pingCount)

        // Then
        assertNotNull("Ping result should not be null", result)
        assertEquals("Host should match the input", host, result.host)
        assertEquals("Average latency should match the data model", 15.5, result.averageLatency ?: 0.0, 0.0001)
        assertEquals("Reachability should match the data model", true, result.isReachable)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when data source fails`() = runTest {
        // Given
        val host = "invalid.host"
        val pingCount = 3
        coEvery { mockDataSource.pingHost(host, pingCount) } throws IllegalArgumentException("Invalid host")

        // When
        pingRepository.pingHost(host, pingCount)
    }
}
