package com.lamti.moviesearch.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.lamti.moviesearch.data.network.ApiService
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.ApiParams.FIRST_PAGE
import com.lamti.moviesearch.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService : ApiService,
                       private val compositeDisposable: CompositeDisposable,
                       private val query: String)
    : PageKeyedDataSource<Int, ApiModel>(){

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ApiModel>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.searchMovies(query, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ApiModel>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.searchMovies(query, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.total_pages >= params.key) {
                            callback.onResult(it.results, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ApiModel>) {

    }
}