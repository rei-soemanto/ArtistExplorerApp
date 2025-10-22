package com.example.artistexplorerapp.data.service

import com.example.artistexplorerapp.data.dto.AlbumResponse
import com.example.artistexplorerapp.data.dto.ArtistResponse
import com.example.artistexplorerapp.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.php")
    suspend fun searchArtist(@Query("s") query: String): ArtistResponse
    @GET("searchalbum.php")
    suspend fun getArtistAlbums(@Query("s") artistName: String): AlbumResponse
    @GET("album.php")
    suspend fun getAlbumDetails(@Query("m") albumId: String): AlbumResponse
    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): TrackResponse
}