package com.lamti.moviesearch.di

import com.lamti.moviesearch.data.repository.DetailsRepository
import com.lamti.moviesearch.data.repository.SearchMoviePagedListRepository
import com.lamti.moviesearch.ui.details.DetailsViewModel
import com.lamti.moviesearch.ui.search.SearchMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchMovieViewModel( SearchMoviePagedListRepository(get(), get()) ) }
    viewModel { DetailsViewModel ( DetailsRepository(get(), get()) ) }
}