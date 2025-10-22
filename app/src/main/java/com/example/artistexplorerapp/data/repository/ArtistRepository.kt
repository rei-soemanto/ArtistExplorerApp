package com.example.artistexplorerapp.data.repository

import com.example.artistexplorerapp.data.service.ApiService

class ArtistRepository(private val apiService: ApiService) {
    suspend fun getArtist(name: String) = apiService.searchArtist(name)
    suspend fun getAlbums(artistName: String) = apiService.getArtistAlbums(artistName)
    suspend fun getAlbumDetails(albumId: String) = apiService.getAlbumDetails(albumId)
    suspend fun getAlbumTracks(albumId: String) = apiService.getAlbumTracks(albumId)
}