package com.example.artistexplorerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.artistexplorerapp.data.dto.Album
import com.example.artistexplorerapp.data.dto.Artist
import com.example.artistexplorerapp.data.dto.Track
import com.example.artistexplorerapp.data.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

data class AlbumDetailScreenData(
    val albumDetails: Album,
    val tracks: List<Track>
)

class AlbumDetailViewModel(
    private val repository: ArtistRepository,
    private val albumId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AlbumDetailScreenData>>(UiState.Loading)
    val uiState: StateFlow<UiState<AlbumDetailScreenData>> = _uiState

    init {
        fetchAlbumDetails()
    }

    fun fetchAlbumDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val detailsDeferred = async { repository.getAlbumDetails(albumId) }
                val tracksDeferred = async { repository.getAlbumTracks(albumId) }

                val detailsResponse = detailsDeferred.await()
                val tracksResponse = tracksDeferred.await()

                val albumDetails = detailsResponse.album?.firstOrNull()
                val tracks = tracksResponse.track ?: emptyList()

                if (albumDetails != null) {
                    _uiState.value = UiState.Success(AlbumDetailScreenData(albumDetails, tracks))
                } else {
                    _uiState.value = UiState.Error("Album details not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

class AlbumDetailViewModelFactory(
    private val repository: ArtistRepository,
    private val albumId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumDetailViewModel(repository, albumId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}