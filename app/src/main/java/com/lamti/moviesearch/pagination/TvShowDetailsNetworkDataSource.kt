package com.lamti.moviesearch.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import com.lamti.moviesearch.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TvShowDetailsNetworkDataSource(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val tvShowNetworkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedTvShowDetailsResponse = MutableLiveData<ApiModel>()
    val downloadedTvShowResponse: LiveData<ApiModel>
        get() = _downloadedTvShowDetailsResponse

    private val _downloadedTvShowTrailerResponse = MutableLiveData<TrailerResponse>()
    val downloadedTvShowTrailerResponse: LiveData<TrailerResponse>
        get() = _downloadedTvShowTrailerResponse

    fun fetchTvShowDetails(tvId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getTvShowDetails(tvId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedTvShowDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (e: Exception) {
        }
    }

    fun fetchTvShowTrailer(tvId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getTvShowTrailer(tvId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedTvShowTrailerResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
        }
    }

}