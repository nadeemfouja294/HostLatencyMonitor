package com.logical.hostlatencymonitor.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.logical.hostlatencymonitor.presentation.viewmodel.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.logical.hostlatencymonitor.presentation.model.PingState
import com.logical.hostlatencymonitor.presentation.ui.components.FailureCard
import com.logical.hostlatencymonitor.presentation.ui.components.LoadingCard
import com.logical.hostlatencymonitor.presentation.ui.components.PingResultList
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.logical.hostlatencymonitor.presentation.model.SortOption
import com.logical.hostlatencymonitor.presentation.ui.components.FailureCard
import com.logical.hostlatencymonitor.presentation.ui.components.LoadingCard
import com.logical.hostlatencymonitor.presentation.ui.components.PingResultList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.pingState.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Host Latency Monitor") },
                actions = {
                    // Only show sort option in Success state
                    if (uiState is PingState.Success) {
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Sort"
                                )
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Sort by Name") },
                                    onClick = {
                                        viewModel.updateSortOption(SortOption.NAME)
                                        showSortMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sort by Latency") },
                                    onClick = {
                                        viewModel.updateSortOption(SortOption.LATENCY)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is PingState.Loading -> {
                    LoadingCard()
                }
                is PingState.Error -> {
                    FailureCard((uiState as PingState.Error).message)
                }
                is PingState.Success -> {
                    val pingResults = (uiState as PingState.Success).results
                    PingResultList(
                        hosts = pingResults,
                        onRefreshHost = { host ->
                            viewModel.retestHost(host.hostUrl)
                        }
                    )
                }
            }
        }
    }
}