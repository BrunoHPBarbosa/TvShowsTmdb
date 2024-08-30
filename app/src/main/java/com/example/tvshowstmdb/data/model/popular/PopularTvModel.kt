package com.example.tvshowstmdb.data.model.popular

data class PopularTvModel(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)