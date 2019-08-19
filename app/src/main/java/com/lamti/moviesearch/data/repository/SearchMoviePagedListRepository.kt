package com.lamti.moviesearch.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lamti.moviesearch.data.local.MovieDao
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.ApiParams.POST_PER_PAGE
import com.lamti.moviesearch.pagination.MovieDataSource
import com.lamti.moviesearch.pagination.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class SearchMoviePagedListRepository (private val apiService : ApiService, private val movieDao: MovieDao) {

    private var moviePagedList: LiveData<PagedList<ApiModel>>? = null
    private var moviesDataSourceFactory: MovieDataSourceFactory? = null

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable, query: String) : LiveData<PagedList<ApiModel>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable, query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory!!, config).build()
        return moviePagedList!!
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory!!.moviesLiveDataSource, MovieDataSource::networkState)
    }

    fun getMovieListFromLocalDB(): LiveData<List<ApiModel>> {
        return movieDao.getMovieList()
    }

    fun removeListeners() {
        moviePagedList = null
        moviesDataSourceFactory = null
    }
}