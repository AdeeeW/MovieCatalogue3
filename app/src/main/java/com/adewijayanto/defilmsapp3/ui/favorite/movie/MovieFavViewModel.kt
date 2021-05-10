package com.adewijayanto.defilmsapp3.ui.favorite.movie

import androidx.lifecycle.ViewModel
import com.adewijayanto.defilmsapp3.data.MovieTvRepository
import com.adewijayanto.defilmsapp3.data.entity.MovieCatalogueEntity

class MovieFavViewModel(private val movieTvRepository: MovieTvRepository) : ViewModel() {
    fun getFavMovies() = movieTvRepository.getFavoriteMovie()

    fun setFavMovie(movieEntity: MovieCatalogueEntity) {
        val newState = !movieEntity.favorite
        movieTvRepository.setFavoriteMovie(movieEntity, newState)
    }

//    fun getFavMovies(): LiveData<PagedList<MovieCatalogueEntity>> =
//        movieTvRepository.getFavoriteMovie()
//
//    fun setFavMovie(movie: MovieCatalogueEntity) {
//        val newState = !movie.favorite
//        movieTvRepository.setFavoriteMovie(movie, newState)
//    }
}