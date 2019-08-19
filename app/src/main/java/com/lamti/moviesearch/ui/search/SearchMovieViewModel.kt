package com.lamti.moviesearch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.repository.NetworkState
import com.lamti.moviesearch.data.repository.SearchMoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class SearchMovieViewModel(private val movieRepository : SearchMoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /*val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable, query)
    }*/

    var moviePagedList : LiveData<PagedList<ApiModel>>? = null
    var networkState: LiveData<NetworkState>? = null

    fun startSearchQuery(query: String) {
        if ( listIsEmpty() ) {
            moviePagedList = movieRepository.fetchLiveMoviePagedList(compositeDisposable, query)
            networkState = movieRepository.getNetworkState()
        }
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList?.value?.isEmpty() ?: true
    }

    fun getWatchList(): LiveData<List<ApiModel>> {
        return movieRepository.getMovieListFromLocalDB()
    }

    fun removeAllListeners() {
        movieRepository.removeListeners()
        moviePagedList = null
        networkState = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}