package com.example.tvshowstmdb.data.model.popular

import java.io.Serializable

data class PopularTvModel(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
):Serializable