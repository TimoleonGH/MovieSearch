package com.lamti.moviesearch.data.network

import com.lamti.moviesearch.data.models.ApiModel
import com.lamti.moviesearch.data.network.responses.MovieResponse
import com.lamti.moviesearch.data.network.responses.TrailerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/multi")
    fun searchMovies(@Query("query") query: String, @Query("page") pageIndex: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<ApiModel>

    @GET("tv/{tv_id}")
    fun getTvShowDetails(@Path("tv_id") id: Int): Single<ApiModel>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(@Path("movie_id") id: Int): Single<TrailerResponse>

    @GET("tv/{tv_id}/videos")
    fun getTvShowTrailer(@Path("tv_id") id: Int): Single<TrailerResponse>
}

object ApiParams {
    const val TIMEOUT = 10
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val FIRST_PAGE = 1
    const val POST_PER_PAGE = 1
}

object Api {
    private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"
    private const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780"
    private const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
    private const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/"

    fun getPosterPath(posterPath: String?): String {
        return BASE_POSTER_PATH + posterPath
    }

    fun getBackdropPath(backdropPath: String): String {
        return BASE_BACKDROP_PATH + backdropPath
    }

    fun getYoutubeVideoPath(videoPath: String): String {
        return YOUTUBE_VIDEO_URL + videoPath
    }

    fun getYoutubeThumbnailPath(thumbnailPath: String): String {
        return "$YOUTUBE_THUMBNAIL_URL$thumbnailPath/default.jpg"
    }
}