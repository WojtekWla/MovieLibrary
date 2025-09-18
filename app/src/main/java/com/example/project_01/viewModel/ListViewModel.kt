package com.example.project_01.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project_01.data.FilmRepository
import com.example.project_01.data.LocalFilmRepository
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status

data class ListViewState(
    val list: List<Film> = LocalFilmRepository.films,
    val selectedFilm: Film? = null,
    val category: Category? = null,
    val status: Status? = null
)

class ListViewModel() : ViewModel() {
    private val repository: FilmRepository = LocalFilmRepository

    var state by mutableStateOf(ListViewState())

    fun loadData() {
        state = ListViewState()
    }

    fun updateList() {
        state = state.copy(
            list = repository.filterList(state.category, state.status)
        )
    }

    fun removeFilm() {
        if (state.selectedFilm != null) {
            repository.removeFilm(state.selectedFilm!!)
        }

        updateList()
    }

    fun updateCategory(category: Category?) {
        state = state.copy(category = category)
    }

    fun updateStatus(status: Status?) {
        state = state.copy(status = status)
    }

    fun updateSelectedFilm(film: Film?) {
        state = state.copy(selectedFilm = film)
    }

    fun getNumberOfPositions(): Int{
        return state.list.size
    }
}