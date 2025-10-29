package com.example.artistexplorerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistexplorerapp.data.dto.Album
import com.example.artistexplorerapp.data.dto.Artist
import com.example.artistexplorerapp.data.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class ArtistScreenData(
    val artist: Artist,
    val albums: List<Album>
)

class ArtistViewModel(private val repository: ArtistRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ArtistScreenData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ArtistScreenData>> = _uiState

    private val artistName = "Hillsong Worship"

    init {
        fetchArtistData()
    }

    fun fetchArtistData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val artistDeferred = async { repository.getArtist(artistName) }
                val albumsDeferred = async { repository.getAlbums(artistName) }

                val artistResponse = artistDeferred.await()
                val albumsResponse = albumsDeferred.await()

                val artist = artistResponse.artists?.firstOrNull()
                val albums = albumsResponse.album ?: emptyList()

                if (artist != null) {
                    _uiState.value = UiState.Success(ArtistScreenData(artist, albums))
                } else {
                    _uiState.value = UiState.Error("Artist not found")
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is UnknownHostException -> "No internet connection. Please check your network."
                    is IOException -> "Could not connect to the server. Please try again."
                    else -> e.message ?: "An unknown error occurred"
                }
                _uiState.value = UiState.Error(errorMessage)
            }
        }
    }
}