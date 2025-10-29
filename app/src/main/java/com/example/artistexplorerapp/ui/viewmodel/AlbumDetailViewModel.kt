package com.example.artistexplorerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.artistexplorerapp.data.dto.Album
import com.example.artistexplorerapp.data.dto.Track
import com.example.artistexplorerapp.data.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

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