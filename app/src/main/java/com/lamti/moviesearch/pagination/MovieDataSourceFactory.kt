package com.lamti.moviesearch.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : ApiService,
                              private val compositeDisposable: CompositeDisposable,
                              private val query: String)
    : DataSource.Factory<Int, ApiModel>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, ApiModel> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable, query)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}