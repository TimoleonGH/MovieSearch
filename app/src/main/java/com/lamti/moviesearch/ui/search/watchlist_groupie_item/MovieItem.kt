package com.lamti.moviesearch.ui.search.watchlist_groupie_item

import android.annotation.SuppressLint
import android.content.Context
import com.lamti.moviesearch.R
import com.lamti.moviesearch.data.network.Api
import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.utils.loadImage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieItem(private val movie: ApiModel, private val context: Context) : Item() {


    companion object {
        private var clickEventListener: MovieItemClickListener? = null
        fun setListener(listener: MovieItemClickListener) {
            clickEventListener = listener
        }
    }

    @SuppressLint("CheckResult")
    override fun bind(viewHolder: ViewHolder, position: Int) {

        if ( movie.title != null ) {
            viewHolder.itemView.movie_title_TV.text = movie.title
            viewHolder.itemView.movie_release_date_TV.text = movie.release_date
        } else {
            viewHolder.itemView.movie_title_TV.text = movie.name
            viewHolder.itemView.movie_release_date_TV.text = movie.first_air_date
        }

        val moviePosterURL = Api.getPosterPath(movie.poster_path)
        viewHolder.itemView.movie_poster_IV.loadImage(context, moviePosterURL, false)

        viewHolder.itemView.setOnClickListener {
            clickEventListener?.onClick(movie)
        }
    }

    override fun getLayout() = R.layout.movie_list_item

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is MovieItem)
            return false
        if (this.movie != other.movie)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? MovieItem)
    }

    override fun hashCode(): Int {
        var result = movie.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    interface MovieItemClickListener {
        fun onClick(movie: ApiModel)
    }
}