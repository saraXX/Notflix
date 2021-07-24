package com.vickikbt.repository.data_source

import androidx.lifecycle.MutableLiveData
import com.vickikbt.cache.AppDatabase
import com.vickikbt.cache.datastore.TimeDatastore
import com.vickikbt.cache.models.PopularResultEntity
import com.vickikbt.repository.mappers.toDomain
import com.vickikbt.domain.models.PopularResult
import com.vickikbt.domain.repository.PopularMoviesRepository
import com.vickikbt.domain.utils.Constants
import com.vickikbt.network.ApiService
import com.vickikbt.repository.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PopularMoviesMoviesRepositoryImpl constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val timeDatastore: TimeDatastore
) : com.vickikbt.network.utils.SafeApiRequest(), PopularMoviesRepository {

    private val popularShowsLiveData = MutableLiveData<PopularResultEntity>()

    init {
        popularShowsLiveData.observeForever { result ->
            //Coroutines.io { savePopularShows(result) }
        }
    }

    override suspend fun savePopularShows(popularResult: PopularResult) {

    }

    override suspend fun fetchPopularMovies(): Flow<PopularResult> {
        val popularShowsDao = appDatabase.popularShowsDao()
        val isPopularShowsCacheAvailable = popularShowsDao.isPopularCacheAvailable() > 0

        val lastSyncTime = timeDatastore.getSyncTime()
        val isTimeSurpassed = com.vickikbt.repository.utils.TimeUtil.isTimeWithinInterval(
            Constants.TimeInterval,
            lastSyncTime,
            System.currentTimeMillis()
        )

        return if (isPopularShowsCacheAvailable && !isTimeSurpassed) {
            popularShowsDao.getPopularShows().map { it.toDomain() }
        } else {

            deletePopularShows()

            val moviesDto = safeApiRequest { apiService.fetchPopularMovies() }
            popularShowsLiveData.postValue(moviesDto.toEntity())

            timeDatastore.saveSyncTime(System.currentTimeMillis())

            //flow { emit(moviesDto.toDomain()) } From network
            return popularShowsDao.getPopularShows().map { it.toDomain() } //From cache database
        }
    }

    /*override suspend fun savePopularShows(popularResultEntity: PopularResultEntity) {
        Coroutines.io {
            appDatabase.popularShowsDao().savePopularShows(popularResultEntity)
        }
    }*/

    private suspend fun deletePopularShows() = appDatabase.upcomingShowsDao().deleteUpcomingShows()

}