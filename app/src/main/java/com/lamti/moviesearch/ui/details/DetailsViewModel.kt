package com.lamti.moviesearch.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import com.lamti.moviesearch.data.repository.DetailsRepository
import com.lamti.moviesearch.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailsViewModel (private val movieRepository : DetailsRepository)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var movieId: Int = 1

    val  movieDetails : LiveData<ApiModel> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val  tvShowDetails : LiveData<ApiModel> by lazy {
        movieRepository.fetchSingleTvDetails(compositeDisposable, movieId)
    }

    val  movieTrailer : LiveData<TrailerResponse> by lazy {
        movieRepository.fetchSingleMovieTrailer(movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    fun setMovieID (id: Int) {
        movieId = id
    }

    fun saveMovieToWatchList(movie: ApiModel) {
        movieRepository.saveMovieToLocalDB(movie)
    }

    fun removeMovieFromWatchList(id: Int) {
        movieRepository.removeMovieFromLocalDB(id)
    }

    fun getWatchListMovie(id: Int): ApiModel {
        return movieRepository.getMovieByIdFromLocalDB(id)
    }

    fun isMovieToWatchList(id: Int): Boolean {
        return movieRepository.isMovieToLocalDB(id)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}