package com.lamti.moviesearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lamti.moviesearch.data.local.converters.*
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.models.Video

@Database(entities = [(ApiModel::class), (Video::class)], version = 1, exportSchema = false)
@TypeConverters(value = [(StringListConverter::class), (IntegerListConverter::class), (VideoListConverter::class), (GenreListConverter::class)])
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}