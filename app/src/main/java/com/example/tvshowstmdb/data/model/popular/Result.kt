package com.example.tvshowstmdb.data.model.popular

import java.io.Serializable

data class Result(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,


):Serializable