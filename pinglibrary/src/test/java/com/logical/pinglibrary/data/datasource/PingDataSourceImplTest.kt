package com.logical.pinglibrary.data.datasource

import com.logical.pinglibrary.data.model.PingResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class PingDataSourceImplTest {

    private val mockRuntime = mockk<Runtime>()
    private val mockProcess = mockk<Process>()

    private lateinit var pingDataSource: PingDataSourceImpl

    @Before
    fun setup() {
        mockkStatic(Runtime::class)
        every { Runtime.getRuntime() } returns mockRuntime
        every { mockRuntime.exec(any<String>()) } returns mockProcess

        pingDataSource = PingDataSourceImpl()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should calculate correct average latency when ping is successful`() = runTest {
        // Given
        val host = "www.google.com"
        val pingCount = 3
        val mockOutput = """
            PING www.google.com (142.250.183.228) 56(84) bytes of data.
            64 bytes from abc.google.com (142.250.183.228): icmp_seq=1 ttl=57 time=15.6 ms
            64 bytes from abc.google.com (142.250.183.228): icmp_seq=2 ttl=57 time=14.8 ms
            64 bytes from abc.google.com (142.250.183.228): icmp_seq=3 ttl=57 time=16.2 ms
        """.trimIndent()
        every { mockProcess.inputStream } returns ByteArrayInputStream(mockOutput.toByteArray())

        // When
        val result = pingDataSource.pingHost(host, pingCount)

        // Then
        assertTrue("Result should indicate success", result.isSuccess)
        val pingResult = result.getOrNull()
        assertNotNull("PingResult should not be null", pingResult)

        // Validate the calculated average latency and other fields
        assertEquals("Average latency should be correctly calculated", 15.5333, pingResult?.averageLatency ?: 0.0, 0.0001)
        assertTrue("Host should be reachable", pingResult?.isReachable ?: false)
        assertEquals("Host should match the input", host, pingResult?.host)
    }

    @Test
    fun `should return unreachable result when ping fails`() = runTest {
        // Given
        val host = "invalid.host"
        val pingCount = 3
        val mockOutput = """
            PING invalid.host (0.0.0.0) 56(84) bytes of data.
            From localhost (127.0.0.1) icmp_seq=1 Destination Host Unreachable
            From localhost (127.0.0.1) icmp_seq=2 Destination Host Unreachable
            From localhost (127.0.0.1) icmp_seq=3 Destination Host Unreachable
        """.trimIndent()
        every { mockProcess.inputStream } returns ByteArrayInputStream(mockOutput.toByteArray())

        // When
        val result = pingDataSource.pingHost(host, pingCount)

        // Then
        assertTrue("Result should indicate success", result.isSuccess)
        val pingResult = result.getOrNull()
        assertNotNull("PingResult should not be null", pingResult)

        // Validate unreachable status and null average latency
        assertNull("Average latency should be null for unreachable host", pingResult?.averageLatency)
        assertFalse("Host should be unreachable", pingResult?.isReachable ?: true)
        assertEquals("Host should match the input", host, pingResult?.host)
    }

    @Test
    fun `should handle empty output gracefully`() = runTest {
        // Given
        val host = "www.example.com"
        val pingCount = 3
        val mockOutput = ""
        every { mockProcess.inputStream } returns ByteArrayInputStream(mockOutput.toByteArray())

        // When
        val result = pingDataSource.pingHost(host, pingCount)

        // Then
        assertTrue("Result should indicate success", result.isSuccess)
        val pingResult = result.getOrNull()
        assertNotNull("PingResult should not be null", pingResult)

        // Validate unreachable status and null average latency
        assertNull("Average latency should be null for empty output", pingResult?.averageLatency)
        assertFalse("Host should be unreachable", pingResult?.isReachable ?: true)
        assertEquals("Host should match the input", host, pingResult?.host)
    }

    @Test
    fun `should fail when ping count is out of range`() = runTest {
        // Given
        val host = "www.google.com"
        val pingCount = 11 // Out of range

        // When
        val result = pingDataSource.pingHost(host, pingCount)

        // Then
        assertTrue("Result should indicate failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertTrue(
            "Exception message should indicate invalid ping count",
            exception is IllegalArgumentException && exception.message?.contains("Ping count must be between") == true
        )
    }

    @Test
    fun `should handle invalid process output format gracefully`() = runTest {
        // Given
        val host = "www.example.com"
        val pingCount = 3
        val mockOutput = """
            Random text that doesn't match ping output
        """.trimIndent()
        every { mockProcess.inputStream } returns ByteArrayInputStream(mockOutput.toByteArray())

        // When
        val result = pingDataSource.pingHost(host, pingCount)

        // Then
        assertTrue("Result should indicate success", result.isSuccess)
        val pingResult = result.getOrNull()
        assertNotNull("PingResult should not be null", pingResult)

        // Validate unreachable status and null average latency
        assertNull("Average latency should be null for invalid output", pingResult?.averageLatency)
        assertFalse("Host should be unreachable", pingResult?.isReachable ?: true)
        assertEquals("Host should match the input", host, pingResult?.host)
    }
}
