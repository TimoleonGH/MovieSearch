package com.lamti.moviesearch.ui.details

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.lamti.moviesearch.R
import com.lamti.moviesearch.data.network.Api
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.models.Video
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import com.lamti.moviesearch.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.lamti.moviesearch.ui.bottom_sheet.SummaryFragment
import com.lamti.moviesearch.utils.loadImage

class DetailsActivity : AppCompatActivity() {

    private val detailsViewModel: DetailsViewModel by viewModel()
    private var movieId: Int = 0
    private var listType: Int = 0
    private var modelType: Int = 0
    private var movieTrailerPosterUrl: String? = null
    private var movieTrailerUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        movieId = intent.getIntExtra("id", 0) // Movie or TvShow id
        listType = intent.getIntExtra("list_type", 0) // 0: network list type / 1: local list type
        modelType = intent.getIntExtra("model_type", 0) // 0: Movie type / 1: TvShow type

        if (listType == 0) getNetworkData() else getLocalData()
    }

    private fun getNetworkData() {
        detailsViewModel.setMovieID(movieId)

        if (modelType == 0) {
            detailsViewModel.movieDetails.observe(this, Observer { movie ->
                bindUI(movie)
            })
        } else {
            detailsViewModel.tvShowDetails.observe(this, Observer { movie ->
                bindUI(movie)
            })
        }

        detailsViewModel.movieTrailer.observe(this, Observer {
            trailerUI(it)
        })

        detailsViewModel.networkState.observe(this, Observer {
            details_progress.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            details_error_TV.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun getLocalData() {
        bindUI(detailsViewModel.getWatchListMovie(movieId))
    }

    private fun bindUI(movie: ApiModel) {

        if (movie.title == null) {
            details_title_TV.text = movie.name
        } else {
            details_title_TV.text = movie.title
        }

        if (!movie.genres.isNullOrEmpty())
            details_genre_TV.text = movie.genres[0].name

        details_poster_IV.loadImage(this, Api.getPosterPath(movie.poster_path), false)

        saveButton(movie)

        if (listType == 1)
            localTrailer(movie.trailerPosterUrl, movie.trailerUrl)

        details_summary_B.setOnClickListener {
            val summaryFragment = SummaryFragment.getInstance(movie.overview)
            summaryFragment.show(supportFragmentManager, summaryFragment.tag)
        }
    }

    private fun localTrailer(posterUrl: String?, trailerUrl: String?) {

        if (posterUrl != null)
            details_trailer_IV.loadImage(this, posterUrl, true)

        details_trailer_IV.setOnClickListener {
            if (trailerUrl != null) {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                try {
                    this.startActivity(webIntent)
                } catch (ex: ActivityNotFoundException) {
                }
            } else {
                Snackbar.make(it, getString(R.string.no_trailer), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveButton(movie: ApiModel) {
        if (detailsViewModel.isMovieToWatchList(movie.id)) {
            details_save_movie_B.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            details_save_movie_B.text = getString(R.string.remove_from_watch_list)
        } else {
            details_save_movie_B.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            details_save_movie_B.text = getString(R.string.add_to_watch_list)
        }

        details_save_movie_B.setOnClickListener {
            if (!detailsViewModel.isMovieToWatchList(movie.id)) {
                movie.trailerPosterUrl = movieTrailerPosterUrl
                movie.trailerUrl = movieTrailerUrl
                detailsViewModel.saveMovieToWatchList(movie)
                details_save_movie_B.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                details_save_movie_B.text = getString(R.string.remove_from_watch_list)
                Snackbar.make(it, getString(R.string.movie_added),Snackbar.LENGTH_SHORT).show()
            } else {
                detailsViewModel.removeMovieFromWatchList(movie.id)
                details_save_movie_B.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                details_save_movie_B.text = getString(R.string.add_to_watch_list)
                Snackbar.make(it, getString(R.string.movie_removed),Snackbar.LENGTH_SHORT).show()
                if( listType == 1 ) finish()
            }
        }
    }

    private fun trailerUI(trailers: TrailerResponse) {
        if (trailers.results.isNotEmpty()) {
            val trailerVideo: Video = trailers.results[0]
            movieTrailerPosterUrl = Api.getYoutubeThumbnailPath(trailerVideo.key)
            movieTrailerUrl = Api.getYoutubeVideoPath(trailerVideo.key)

            if (movieTrailerPosterUrl != null)
                details_trailer_IV.loadImage(this, movieTrailerPosterUrl!!, true)

            details_trailer_IV.setOnClickListener {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(movieTrailerUrl))
                try {
                    this.startActivity(webIntent)
                } catch (ex: ActivityNotFoundException) {
                }
            }
        } else {
            details_trailer_IV.setOnClickListener {
                Snackbar.make(it, getString(R.string.no_trailer), Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}
