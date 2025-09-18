package com.example.project_01.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project_01.data.FilmRepository
import com.example.project_01.data.LocalFilmRepository
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status
import java.time.LocalDate


open class EditFilmViewModel : ViewModel(){
    private var errorMessage = ""
    private val repository: FilmRepository = LocalFilmRepository
    var state: Film? by mutableStateOf(null)

    fun loadData(id: Int) {
        var film = repository.findById(id)
        state = film
    }

    fun updateTitle(title: String) {
        state = state?.copy(
            title = title
        )
    }

    fun updateCategory(category: Category) {
        state = state?.copy(
            category = category
        )
    }

    fun updateStatus(status: Status) {
        state = state?.copy(
            status = status
        )
    }

    fun updateLogo(uri: Uri?) {
        state = state?.copy(
            logoInt = null,
            logoString = uri.toString(),
        )
    }

    fun updateDate(date: LocalDate?) {
        if(date != null) {
            state = state?.copy(
                releaseDate = date
            )
        }
    }

    fun updateRate(rate: Int) {
        state = state?.copy(
            rate = rate
        )
    }

    fun updateComment(comment: String) {
        state = state?.copy(
            comment = comment
        )
    }

    fun saveFilm(): Boolean {
        val validated = validateData()
        if (validated) {
            return repository.updateFilm(state)
        }

        return false
    }

    fun getErrorMessage(): String {
        return errorMessage
    }

    private fun validateData(): Boolean {
        if(state!!.title.isEmpty()) {
            errorMessage = "Title cannot be empty"
            return false
        }
        else if (state!!.releaseDate.isAfter(LocalDate.now().plusYears(2))) {
            errorMessage = "Selected date is to far in the future"
            return false
        }
        else if(state!!.status == Status.Seen && state!!.rate < 0) {
            errorMessage = "Rate is not set"
            return false
        }
        else if (state!!.status == Status.Seen && state!!.comment.isEmpty()) {
            errorMessage = "Comment is invalid"
            return false
        }
        else if(state!!.logoString == null && state!!.logoInt == null) {
            errorMessage = "Image is not set"
            return false
        }

        errorMessage = ""
        return true
    }
}