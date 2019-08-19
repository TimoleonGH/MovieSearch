package com.lamti.moviesearch.data.models

import androidx.room.Entity

@Entity(primaryKeys = [("id")])
data class Video(
    val id: String,
    val name: String,
    val site: String,
    val key: String,
    val size: Int,
    val type: String
)