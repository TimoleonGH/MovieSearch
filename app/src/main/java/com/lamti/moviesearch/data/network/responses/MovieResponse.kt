package com.lamti.moviesearch.data.network.responses

import com.lamti.moviesearch.data.models.ApiModel

data class MovieResponse(
    val page: Int,
    val results: List<ApiModel>,
    val total_results: Int,
    val total_pages: Int
)