package com.lamti.moviesearch.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.models.Video

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movies: List<ApiModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: ApiModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(video: Video)

    @Update
    fun updateMovie(movie: ApiModel)

    @Query("SELECT * FROM ApiModel WHERE id = :id_")
    fun getMovie(id_: Int): ApiModel?

    @Query("SELECT * FROM ApiModel")
    fun getMovieList(): LiveData<List<ApiModel>>

    @Query("DELETE FROM ApiModel WHERE id = :id_")
    fun removeMovie(id_: Int)

    @Query("DELETE FROM Video WHERE id = :id_")
    fun removeVideo(id_: String)
}