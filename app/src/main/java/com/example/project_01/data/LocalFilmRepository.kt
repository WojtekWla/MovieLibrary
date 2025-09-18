package com.example.project_01.data

import com.example.project_01.R
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status
import java.time.LocalDate

object LocalFilmRepository : FilmRepository {
    private val _films: MutableList<Film>
    override val films: List<Film>
        get() = _films.sortedBy { it.releaseDate }

    override fun filterList(
        category: Category?,
        status: Status?
    ): List<Film> {
        var tmp: List<Film> = films
        if(category != null) {
            tmp = tmp.filter { it.category == category }
        }

        if(status != null) {
            tmp = tmp.filter { it.status == status }
        }

        return tmp
    }

    override fun removeFilm(film: Film) {
        _films.remove(film)
    }

    override fun updateStatus(id: Int, status: Status) {
        var film = findById(id)
        if(film != null) {
            film.status = status
        }
    }

    override fun saveFilm(film: Film): Boolean {
        if (!films.contains(film)) {
            var added = _films.add(film)
            return added
        }

        return false
    }

    override fun updateFilm(film: Film?): Boolean {
        var searchedFilm = _films.find { it.id == film?.id }
        if(searchedFilm != null) {
            searchedFilm.title = film!!.title
            searchedFilm.status = film.status
            searchedFilm.category = film.category
            searchedFilm.releaseDate = film.releaseDate
            searchedFilm.logoString = film.logoString
            searchedFilm.logoInt = film.logoInt
            searchedFilm.rate = film.rate
            searchedFilm.comment = film.comment
            return true
        }else {
            return false
        }
    }

    init {
        _films = mutableListOf<Film>(
            Film(
                id = 1,
                logoInt = R.drawable.tron,
                title = "Tron Legacy",
                releaseDate = LocalDate.of(2010, 1, 1),
                category = Category.Film,
                status = Status.Seen,
                rate = 5,
                comment = "Epic"
            ),
            Film(
                id = 2,
                logoInt = R.drawable.inception,
                title = "Inception",
                releaseDate = LocalDate.of(2010, 7,30),
                category = Category.Film,
                status = Status.Unseen
            ),
            Film(
                id = 3,
                logoInt = R.drawable.oppenheimer,
                title = "Oppenheimer",
                releaseDate = LocalDate.of(2023, 7,21),
                category = Category.Film,
                status = Status.Seen,
                rate = 4,
                comment = "Okay"
            ),
            Film(
                id = 4,
                logoInt = R.drawable.got,
                title = "Game Of Thrones",
                releaseDate = LocalDate.of(2011, 4,17),
                category = Category.Series,
                status = Status.Unseen
            ),
        )
    }
}