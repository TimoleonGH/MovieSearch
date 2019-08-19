package com.lamti.moviesearch.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.lamti.moviesearch.data.local.AppDatabase
import com.lamti.moviesearch.data.local.MovieDao
import com.lamti.moviesearch.data.repository.DetailsRepository
import com.lamti.moviesearch.data.repository.SearchMoviePagedListRepository
import com.lamti.moviesearch.ui.search.SearchMoviePagedListAdapter
import org.koin.dsl.module

val repositoryModule = module {
    single { provideDatabase(get()) }
    single { provideMovieDao(get()) }
    single { SearchMoviePagedListRepository(get(), get()) }
    single { SearchMoviePagedListAdapter(get()) }
    single { DetailsRepository(get(), get()) }
}


fun provideDatabase(@NonNull application: Application): AppDatabase {
    return Room
        .databaseBuilder(application, AppDatabase::class.java, "MovieSearch.db")
        .allowMainThreadQueries()
        .build()
}

fun provideMovieDao(@NonNull database: AppDatabase): MovieDao { return database.movieDao() }