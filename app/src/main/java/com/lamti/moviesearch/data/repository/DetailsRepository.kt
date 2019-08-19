package com.lamti.moviesearch.data.repository

import androidx.lifecycle.LiveData
import com.lamti.moviesearch.data.local.MovieDao
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import com.lamti.moviesearch.pagination.MovieDetailsNetworkDataSource
import com.lamti.moviesearch.pagination.TvShowDetailsNetworkDataSource
import io.reactivex.disposables.CompositeDisposable

class DetailsRepository(private val apiService: ApiService, private val movieDao: MovieDao) {

    private var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource? = null
    private lateinit var tvShowDetailsNetworkDataSource: TvShowDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<ApiModel> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource!!.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource!!.downloadedMovieResponse
    }

    fun fetchSingleTvDetails(compositeDisposable: CompositeDisposable, tvId: Int): LiveData<ApiModel> {
        tvShowDetailsNetworkDataSource = TvShowDetailsNetworkDataSource(apiService, compositeDisposable)
        tvShowDetailsNetworkDataSource.fetchTvShowDetails(tvId)
        return tvShowDetailsNetworkDataSource.downloadedTvShowResponse
    }

    fun fetchSingleMovieTrailer(movieId: Int): LiveData<TrailerResponse> {

        return if (movieDetailsNetworkDataSource != null) {
            movieDetailsNetworkDataSource!!.fetchMovieTrailer(movieId)
            movieDetailsNetworkDataSource!!.downloadedMovieTrailerResponse
        } else {
            tvShowDetailsNetworkDataSource.fetchTvShowTrailer(movieId)
            tvShowDetailsNetworkDataSource.downloadedTvShowTrailerResponse
        }
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return if (movieDetailsNetworkDataSource != null)
            movieDetailsNetworkDataSource!!.networkState
        else
            tvShowDetailsNetworkDataSource.tvShowNetworkState
    }

    fun saveMovieToLocalDB(movie: ApiModel) = movieDao.insertMovie(movie)

    fun removeMovieFromLocalDB(id: Int) = movieDao.removeMovie(id)

    fun isMovieToLocalDB(id: Int): Boolean = movieDao.getMovie(id) != null

    fun getMovieByIdFromLocalDB(id: Int): ApiModel = movieDao.getMovie(id)!!
}