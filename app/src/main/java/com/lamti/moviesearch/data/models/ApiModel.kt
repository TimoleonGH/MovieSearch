package com.lamti.moviesearch.data.models

import androidx.room.Entity

@Entity(primaryKeys = [("id")])
data class ApiModel(
    val id: Int,
    val poster_path: String,
    var videos: List<Video>? = ArrayList(),
    val genres: List<Genre>? = ArrayList(),
    val overview: String,
    var trailerUrl: String?,
    var trailerPosterUrl: String?,

    val release_date: String?,
    val title: String?,

    val first_air_date: String?,
    val name: String?
)