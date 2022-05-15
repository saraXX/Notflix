package com.vickikbt.shared.data.cache.sqldelight.daos

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.vickikbt.shared.data.cache.sqldelight.AppDatabase
import com.vickikbt.shared.data.cache.sqldelight.MovieEntity
import com.vickikbt.shared.domain.utils.Constants.CATEGORY_NOW_PLAYING_MOVIES
import com.vickikbt.shared.domain.utils.DatabaseDriverFactory
import kotlinx.coroutines.flow.map

class MoviesDao constructor(private val databaseDriverFactory: DatabaseDriverFactory) {

    private val appDatabase = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = appDatabase.appDatabaseQueries

    fun saveMovies(movieEntity: MovieEntity) {
        dbQuery.transaction {
            dbQuery.saveMovies(movieEntity)
        }
    }

    val getNowPlayingMovies =
        dbQuery.getNowPlayingMovies(category = CATEGORY_NOW_PLAYING_MOVIES).asFlow()
            .map { it.executeAsList() }

    fun getMovies(category: String) =
        dbQuery.getMovies(category = category).asFlow().map { it.executeAsList() }

    fun deleteMovies(category: String) = dbQuery.deleteMovies(category = category)

    fun isCategoryCacheAvailable(category: String) =
        dbQuery.isCategoryCacheAvailable(category = category).executeAsOne()

    val getFavouriteMovies = dbQuery.getFavouriteMovies().asFlow().map { it.executeAsList() }

    fun isMovieFavorite(movieId: Int) =
        dbQuery.isMovieFavourite(id = movieId).asFlow().map { it.executeAsOneOrNull()?.isFavourite }

    fun updateIsMovieFavorite(cacheId: Int, isFavourite: Boolean) {
        dbQuery.transaction {
            dbQuery.updateIsMovieFavorite(cacheId = cacheId, isFavourite = isFavourite)
        }
    }
}
