package ui.screens.favourites

import cafe.adriel.voyager.core.model.ScreenModel
import com.vickikbt.shared.domain.models.MovieDetails
import com.vickikbt.shared.domain.repositories.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouritesScreenModel constructor(private val favouritesRepository: FavoritesRepository) :
    ScreenModel {

    private val _favouriteMovies = MutableStateFlow<List<MovieDetails>?>(emptyList())
    val favouriteMovies get() = _favouriteMovies.asStateFlow()

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() = CoroutineScope(Dispatchers.IO).launch {
        favouritesRepository.getFavouriteMovies().collectLatest { movies ->
            _favouriteMovies.value = movies
        }
    }
}
