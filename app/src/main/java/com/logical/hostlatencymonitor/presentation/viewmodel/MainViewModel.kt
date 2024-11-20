package com.logical.hostlatencymonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logical.hostlatencymonitor.domain.model.Host
import com.logical.hostlatencymonitor.domain.usecase.FetchHostsUseCase
import com.logical.hostlatencymonitor.domain.usecase.PingHostUseCaseWrapper
import com.logical.hostlatencymonitor.presentation.model.PingState
import com.logical.hostlatencymonitor.presentation.model.PresentationPingResult
import com.logical.hostlatencymonitor.presentation.model.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchHostsUseCase: FetchHostsUseCase,
    private val pingHostUseCase: PingHostUseCaseWrapper
) : ViewModel() {
    private val JsonUrl = "https://gist.githubusercontent.com/anonymous/290132e587b77155eebe44630fcd12cb/raw/777e85227d0c1c16e466475bb438e0807900155c/sk_hosts"

    private var hosts = emptyList<Host>()
    private var currentSortOption = SortOption.LATENCY
    private val _pingState = MutableStateFlow<PingState>(PingState.Loading)
    val pingState: StateFlow<PingState> = _pingState

    init {
        fetchAndPingHosts()
    }

    fun updateSortOption(sortOption: SortOption) {
        currentSortOption = sortOption
        // Re-sort current results
        val currentState = _pingState.value
        if (currentState is PingState.Success) {
            _pingState.value = PingState.Success(sortResults(currentState.results))
        }
    }

    private fun sortResults(results: List<PresentationPingResult>): List<PresentationPingResult> {
        return when (currentSortOption) {
            SortOption.NAME -> results.sortedBy { it.name }
            SortOption.LATENCY -> results.sortedBy { it.averageLatency ?: Float.MAX_VALUE }
        }
    }

    private fun fetchAndPingHosts(pingCount: Int = 5) {
        viewModelScope.launch {
            try {
                _pingState.value = PingState.Loading
                hosts = fetchHostsUseCase(JsonUrl).getOrThrow()

                val pingJobs = hosts.map { host ->
                    async(Dispatchers.IO) {
                        pingHost(host, pingCount)
                    }
                }

                val allResults = pingJobs.awaitAll()
                // Sort results before updating state
                _pingState.value = PingState.Success(sortResults(allResults))

            } catch (e: Exception) {
                _pingState.value = PingState.Error("Failed to fetch or process hosts.")
            }
        }
    }

    private suspend fun pingHost(host: Host, pingCount: Int): PresentationPingResult {
        return try {
            val pingResult = pingHostUseCase(host.url, pingCount).getOrThrow()
            PresentationPingResult(
                name = host.name,
                hostUrl = host.url,
                iconUrl = host.icon,
                averageLatency = pingResult.averageLatency?.toFloat(),
                success = true,
                isLoading = false
            )
        } catch (e: Exception) {
            PresentationPingResult(
                name = host.name,
                hostUrl = host.url,
                iconUrl = host.icon,
                averageLatency = null,
                success = false,
                isLoading = false
            )
        }
    }

    fun retestHost(hostUrl: String, pingCount: Int = 5) {
        viewModelScope.launch {
            val host = hosts.find { it.url == hostUrl } ?: return@launch

            // Set loading state for the specific host
            val currentState = _pingState.value
            if (currentState is PingState.Success) {
                val updatedResults = currentState.results.map {
                    if (it.hostUrl == host.url) it.copy(isLoading = true) else it
                }
                _pingState.value = PingState.Success(sortResults(updatedResults))
            }

            // Ping test and update result
            val result = pingHost(host, pingCount)
            if (_pingState.value is PingState.Success) {
                val updatedResults = (_pingState.value as PingState.Success).results.map {
                    if (it.hostUrl == host.url) result else it
                }
                _pingState.value = PingState.Success(sortResults(updatedResults))
            }
        }
    }
}