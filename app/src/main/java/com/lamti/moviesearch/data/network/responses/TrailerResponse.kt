package com.lamti.moviesearch.data.network.responses

import com.lamti.moviesearch.data.models.Video

data class TrailerResponse(
    val id: Int,
    val results: List<Video>
)