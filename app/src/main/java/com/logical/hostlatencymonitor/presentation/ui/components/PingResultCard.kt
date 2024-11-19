package com.logical.hostlatencymonitor.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.logical.hostlatencymonitor.R
import com.logical.hostlatencymonitor.presentation.model.PresentationPingResult

@Composable
fun PingResultList(
    hosts: List<PresentationPingResult>,
    onRefreshHost: (PresentationPingResult) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hosts.size) { index ->
            val host = hosts[index]
            PingResultCard(
                state =host,
                onRefresh = { onRefreshHost(host) }
            )
        }
    }
}

@Composable
fun PingResultCard(
    state: PresentationPingResult,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Host Icon
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.iconUrl)
                            .crossfade(true)
                            .error(R.drawable.ic_error)
                            .placeholder(R.drawable.ic_broken_image)
                            .build()
                    ),
                    contentDescription = "Host Icon",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Host Name
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            // Latency and Refresh
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (state.averageLatency == null) "Unreachable"
                        else "Latency: ${state.averageLatency} ms",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    enabled = !state.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Latency",
                        tint = if (state.isLoading)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}