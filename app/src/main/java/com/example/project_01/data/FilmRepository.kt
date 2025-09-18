package com.example.project_01.data

import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status

interface FilmRepository {
    val films: List<Film>

    fun findById(id: Int): Film? {
        return films.find { it.id == id }
    }

    fun filterList(category: Category?, status: Status?): List<Film>
    fun removeFilm(film: Film)
    fun updateStatus(id: Int, status: Status)
    fun saveFilm(film: Film): Boolean
    fun updateFilm(film: Film?): Boolean
}