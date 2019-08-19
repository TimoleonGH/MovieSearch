package com.lamti.moviesearch.di

import android.content.Context
import com.lamti.moviesearch.BuildConfig
import com.lamti.moviesearch.data.network.ApiParams.BASE_URL
import com.lamti.moviesearch.data.network.ApiParams.TIMEOUT
import com.lamti.moviesearch.data.network.ApiService
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { createOkHttpCache(get()) }
    single(named("logging")) { createLoggingInterceptor() }
    single(named("header")) { createHeaderInterceptor() }
    single { createOkHttpClient(get(named("logging")), get(named("header"))) }
    single { createAppRetrofit(get()) }
    single { createApiService(get()) }
}

fun createOkHttpCache(context: Context): Cache =
    Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

fun createLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

fun createHeaderInterceptor(): Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newUrl = request.url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .method(request.method(), request.body())
            .build()
        chain.proceed(newRequest)
    }

fun createOkHttpClient(logging: Interceptor, header: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(header)
        .addInterceptor(logging)
        .build()

fun createAppRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

fun createApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)