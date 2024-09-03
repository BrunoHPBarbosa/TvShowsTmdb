package com.example.tvshowstmdb.data.model.seasons

data class Episode(
    val episode_number: Int,
    val episode_type: String,
    val id: Int,
    val name: String,
    val overview: String,
    val runtime: Int,
    val season_number: Int,
    val show_id: Int,
    val still_path: String,
)