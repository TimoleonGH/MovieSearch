package com.lamti.moviesearch.data.models

import androidx.room.Entity

@Entity(primaryKeys = [("id")])
data class TvShow(
    val first_air_date: String,
    var videos: List<Video>? = ArrayList(),
    val genres: List<Genre>,
    val name: String,
    val overview: String,
    val poster_path: String
)