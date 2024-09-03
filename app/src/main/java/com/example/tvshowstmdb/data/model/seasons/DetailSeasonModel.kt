package com.example.tvshowstmdb.data.model.seasons

data class DetailSeasonModel(
    val _id: String,
    val episodes: List<Episode>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val season_number: Int,
)