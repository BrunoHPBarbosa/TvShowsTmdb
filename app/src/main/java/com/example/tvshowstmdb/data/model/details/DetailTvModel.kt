package com.example.tvshowstmdb.data.model.details

data class DetailTvModel(

    val adult: Boolean,
    val backdrop_path: String,
    val episode_run_time: List<Any>,
    val genres: List<Genre>,
    val id: Int,
    val name: String,
    val next_episode_to_air: Any,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val overview: String,
    val poster_path: String,
    val seasons: List<Season>,

)