package com.example.tvshowstmdb.repositorio

import com.example.tvshowstmdb.data.model.remote.ServiceApi
import javax.inject.Inject

class TvShowRespository @Inject constructor(
    private val api : ServiceApi
) {

    suspend fun getPopularTvs() = api.getPopularTvs()

    suspend fun getDetailsTvs(seriesId: Int) = api.getDetalhesTvs(seriesId)

    suspend fun getKeywords(seriesId: Int) = api.getKeywords(seriesId)

    suspend fun getTemporadasTvs(seriesId: Int,seasonNumber: Int) = api.getTemporadasTvs(seriesId,seasonNumber)

    suspend fun searchTvShows(query:String) = api.searchTvs(query)

}