package com.lamti.moviesearch.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.lamti.moviesearch.R
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.repository.NetworkState
import com.lamti.moviesearch.ui.details.DetailsActivity
import com.lamti.moviesearch.ui.search.watchlist_groupie_item.MovieItem
import com.lamti.moviesearch.utils.hideKeyboard
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject


class SearchActivity : AppCompatActivity(), MovieItem.MovieItemClickListener {

    private val searchMovieViewModel: SearchMovieViewModel by viewModel()
    private val movieAdapter: SearchMoviePagedListAdapter by inject()

    private var localMoviesObserver: Observer<List<ApiModel>>? = null
    private var networkMoviesObserver: Observer<PagedList<ApiModel>>? = null
    private var networkStateObserver: Observer<NetworkState>? = null

    private var searchMovieListVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initRecyclerView()
        initClickListeners()
        watchListButtonVisibilityListener()
    }

    private fun initRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        search_movie_RV.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun initClickListeners() {

        MovieItem.setListener(this)

        search_B.setOnClickListener {
            if (search_ET.text!!.isNotEmpty()) {
                it.hideKeyboard()
                search_root_CL.requestFocus()
                showSearchList(true)
                startMovieSearch(search_ET.text.toString())
            } else {
                Snackbar.make(it, "Type a movie name first", Snackbar.LENGTH_SHORT).show()
            }
        }

        watch_list_B.setOnClickListener {
            showSearchList(!searchMovieListVisible)
        }
    }

    private fun watchListButtonVisibilityListener() {
        searchMovieViewModel.getWatchList().observe(this, Observer {
            if ( it.isNullOrEmpty() ) {
                watch_list_B.visibility = View.GONE
                showSearchList(true)
            } else
                watch_list_B.visibility = View.VISIBLE

        })
    }

    private fun showSearchList(b: Boolean) {
        searchMovieListVisible = b
        if ( b ) {
            search_movie_RV.visibility = View.VISIBLE
            watch_list_RV.visibility = View.GONE
            watch_list_B.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite))
        } else {
            search_movie_RV.visibility = View.GONE
            watch_list_RV.visibility = View.VISIBLE
            watch_list_B.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_trailer))

            getLocalDbMovies()
        }
    }

    private fun startMovieSearch(searchQuery: String) {

        if ( searchMovieViewModel.moviePagedList != null) {
            searchMovieViewModel.removeAllListeners()
        }

        searchMovieViewModel.startSearchQuery(searchQuery)

        networkMoviesObserver = Observer {
            movieAdapter.submitList(it)
        }

        networkStateObserver = Observer {
            search_movie_progress.visibility =
                if (searchMovieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            search_movie_TV.visibility =
                if (searchMovieViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!searchMovieViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        }

        searchMovieViewModel.moviePagedList?.observe(this, networkMoviesObserver!!)
        searchMovieViewModel.networkState?.observe(this, networkStateObserver!!)
    }

    private fun getLocalDbMovies() {
        var moviesSection: Section

        localMoviesObserver = Observer {
            val movies = ArrayList<Item>()
            it.forEach { movie ->
                movies.add(MovieItem(movie, this@SearchActivity))
            }

            watch_list_RV.apply {
                layoutManager = GridLayoutManager(context, 3)
                val groupAdapter = GroupAdapter<ViewHolder>().apply {
                    moviesSection = Section(movies)
                    this.add(moviesSection)
                } as RecyclerView.Adapter<*>
                this.adapter = groupAdapter
            }
        }

        searchMovieViewModel.getWatchList().observe(this, localMoviesObserver!!)
    }

    override fun onClick(movie: ApiModel) {
        val intent = Intent(this@SearchActivity, DetailsActivity::class.java)
        intent.putExtra("id", movie.id)
        intent.putExtra("list_type", 1)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if ( searchMovieViewModel.moviePagedList != null) {
            searchMovieViewModel.removeAllListeners()
            movieAdapter.submitList(null)
        }
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("movie_list_visible", searchMovieListVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        search_ET.hideKeyboard()
        searchMovieListVisible = savedInstanceState.getBoolean("movie_list_visible")
        showSearchList(searchMovieListVisible)
    }

}
