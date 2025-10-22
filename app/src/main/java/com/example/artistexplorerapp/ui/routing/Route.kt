package com.example.artistexplorerapp.ui.routing

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.artistexplorerapp.data.repository.ArtistRepository
import com.example.artistexplorerapp.data.service.RetrofitClient
import com.example.artistexplorerapp.ui.view.AlbumDetailScreen
import com.example.artistexplorerapp.ui.view.ArtistDetail
import com.example.artistexplorerapp.ui.viewmodel.AlbumDetailViewModel
import com.example.artistexplorerapp.ui.viewmodel.AlbumDetailViewModelFactory
import com.example.artistexplorerapp.ui.viewmodel.ArtistViewModel

object Routes {
    const val ARTIST_DETAIL = "artistDetail"
    const val ALBUM_DETAIL_ROUTE = "albumDetail"
    const val ALBUM_DETAIL_ARG = "albumId"
    const val ALBUM_DETAIL = "$ALBUM_DETAIL_ROUTE/{$ALBUM_DETAIL_ARG}"
}

@Composable
fun Route() {
    val navController = rememberNavController()
    val apiService = RetrofitClient.instance
    val repository = ArtistRepository(apiService)

    NavHost(navController = navController, startDestination = Routes.ARTIST_DETAIL) {

        composable(Routes.ARTIST_DETAIL) {
            val artistViewModel: ArtistViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ArtistViewModel(repository) as T
                    }
                }
            )
            ArtistDetail(
                viewModel = artistViewModel,
                onAlbumClick = { albumId ->
                    navController.navigate("${Routes.ALBUM_DETAIL_ROUTE}/$albumId")
                }
            )
        }

        composable(
            route = Routes.ALBUM_DETAIL,
            arguments = listOf(navArgument(Routes.ALBUM_DETAIL_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString(Routes.ALBUM_DETAIL_ARG)

            if (albumId == null) {
                navController.popBackStack()
                return@composable
            }

            val albumDetailViewModel: AlbumDetailViewModel = viewModel(
                factory = AlbumDetailViewModelFactory(repository, albumId)
            )

            AlbumDetailScreen(
                viewModel = albumDetailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}