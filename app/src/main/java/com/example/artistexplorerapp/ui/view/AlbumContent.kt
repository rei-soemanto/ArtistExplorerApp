package com.example.artistexplorerapp.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.artistexplorerapp.ui.viewmodel.AlbumDetailScreenData

@Composable
fun AlbumContent(data: AlbumDetailScreenData) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AlbumHeader(album = data.albumDetails)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Tracks",
                color = Color(0xFFFFB700),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        itemsIndexed(data.tracks) { index, track ->
            TrackItem(track = track, trackNumber = index + 1)
            Divider(color = MaterialTheme.colorScheme.outline)
        }
    }
}