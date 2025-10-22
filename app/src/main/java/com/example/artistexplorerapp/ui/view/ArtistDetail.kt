package com.example.artistexplorerapp.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.artistexplorerapp.ui.viewmodel.ArtistViewModel
import com.example.artistexplorerapp.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetail(
    viewModel: ArtistViewModel,
    onAlbumClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = (uiState as? UiState.Success)?.data?.artist?.strArtist ?:
                    (uiState as? UiState.Loading)?.let { "Loading..." } ?: "Error"
                    Text(text = title)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingComponent()
                is UiState.Error -> ErrorComponent(message = state.message)
                is UiState.Success -> ArtistContent(
                    data = state.data,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}