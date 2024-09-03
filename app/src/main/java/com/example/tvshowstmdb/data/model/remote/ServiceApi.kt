package com.example.tvshowstmdb.data.model.remote

import com.example.tvshowstmdb.data.model.details.DetalhesTvModel
import com.example.tvshowstmdb.data.model.keywords.keywordsModel
import com.example.tvshowstmdb.data.model.popular.PopularTvModel
import com.example.tvshowstmdb.data.model.popular.Result
import com.example.tvshowstmdb.data.model.temporadas.DetalhesTemporadasModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("tv/popular")
    suspend fun getPopularTvs(

    ): Response<PopularTvModel>


    @GET("tv/{series_id}")
    suspend fun getDetalhesTvs(
        @Path(
            value = "series_id",
            encoded = true
        ) seriesId : Int
    ): Response<DetalhesTvModel>

    @GET("tv/{series_id}/keywords")
    suspend fun getKeywords(
        @Path(
            value = "series_id",
            encoded = true
        ) seriesId : Int
    ): Response<keywordsModel>

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getTemporadasTvs(
        @Path(value = "series_id", encoded = true) seriesId : Int,
        @Path("season_number", encoded = true) seasonNumber: Int
    ): Response<DetalhesTemporadasModel>

@GET("search/tv")
suspend fun searchTvs(@Query("query") query: String): Response<PopularTvModel>

}