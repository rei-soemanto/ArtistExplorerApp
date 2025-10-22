package com.example.artistexplorerapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.artistexplorerapp.data.dto.Artist

@Composable
fun ArtistHeader(artist: Artist) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Box {
                AsyncImage(
                    model = artist.strArtistFanart ?: artist.strArtistBanner ?: artist.strArtistThumb,
                    contentDescription = artist.strArtist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Color.Black.copy(alpha = 0.6f)
                        )
                )
                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        artist.strArtist,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        artist.strGenre ?: "Unknown Genre",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}