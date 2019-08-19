package com.lamti.moviesearch.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import com.lamti.moviesearch.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource (private val apiService : ApiService,
                                     private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedMovieDetailsResponse =  MutableLiveData<ApiModel>()
    val downloadedMovieResponse: LiveData<ApiModel>
        get() = _downloadedMovieDetailsResponse

    private val _downloadedMovieTrailerResponse =  MutableLiveData<TrailerResponse>()
    val downloadedMovieTrailerResponse: LiveData<TrailerResponse>
        get() = _downloadedMovieTrailerResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)

                        }
                    )
            )
        }
        catch (e: Exception){
            _networkState.postValue(NetworkState.ERROR)
        }
    }

    fun fetchMovieTrailer(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getMovieTrailer(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieTrailerResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        }
        catch (e: Exception){
            _networkState.postValue(NetworkState.ERROR)
        }
    }

}