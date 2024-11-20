package com.logical.hostlatencymonitor.domain.usecase

import com.logical.pinglibrary.domain.model.PingResult
import com.logical.pinglibrary.domain.usecase.PingHostUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PingHostUseCaseWrapperTest {

    private val mockPingHostUseCase = mockk<PingHostUseCase>()

    private lateinit var pingHostUseCaseWrapper: PingHostUseCaseWrapper

    @Before
    fun setup() {
        pingHostUseCaseWrapper = PingHostUseCaseWrapper(mockPingHostUseCase)
    }

    @Test
    fun `should return success when pingHostUseCase returns valid data`() = runTest {
        // Given
        val host = "www.google.com"
        val pingCount = 3
        val domainPingResult = PingResult(
            host = host,
            averageLatency = 15.5,
            isReachable = true
        )
        coEvery { mockPingHostUseCase(host, pingCount) } returns domainPingResult

        // When
        val result = pingHostUseCaseWrapper(host, pingCount)

        // Then
        assertTrue("Result should indicate success", result.isSuccess)
        val pingResult = result.getOrNull()
        assertNotNull("PingResult should not be null", pingResult)
        assertEquals("Host should match the input", host, pingResult?.host)
        assertEquals("Average latency should match", 15.5, pingResult?.averageLatency ?: 0.0, 0.0001)
        assertTrue("Host should be reachable", pingResult?.isReachable ?: false)
    }

    @Test
    fun `should return failure when pingHostUseCase throws exception`() = runTest {
        // Given
        val host = "invalid.host"
        val pingCount = 3
        val exception = RuntimeException("Ping failed")
        coEvery { mockPingHostUseCase(host, pingCount) } throws exception

        // When
        val result = pingHostUseCaseWrapper(host, pingCount)

        // Then
        assertTrue("Result should indicate failure", result.isFailure)
        val error = result.exceptionOrNull()
        assertNotNull("Exception should not be null", error)
        assertEquals("Exception should match the one thrown by use case", exception, error)
    }
}
