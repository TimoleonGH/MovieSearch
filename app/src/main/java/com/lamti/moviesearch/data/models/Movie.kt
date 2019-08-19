package com.lamti.moviesearch.data.models

import androidx.room.Entity

@Entity(primaryKeys = [("id")])
data class Movie(
    val id: Int,
    val poster_path: String,
    val release_date: String,
    val title: String,
    var videos: List<Video>? = ArrayList(),
    val genres: List<Genre>? = ArrayList(),
    var trailerUrl: String?,
    val overview: String
)

