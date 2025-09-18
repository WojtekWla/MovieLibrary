package com.example.project_01.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project_01.data.FilmRepository
import com.example.project_01.data.LocalFilmRepository
import com.example.project_01.model.Film
import com.example.project_01.model.Status

 class FilmViewModel : ViewModel() {
    private val repository: FilmRepository = LocalFilmRepository

    var state by mutableStateOf<Film?>(null)

    fun loadData(id: Int) {
        state = repository.findById(id)
    }

     fun updateState(film: Film) {
         state = film
     }

    fun updateFilmStatus(film: Film, status: Status) {
        val newFilm = film.copy(status = status)
        repository.updateStatus(newFilm.id, status)
        updateState(newFilm)
    }
}