package com.example.project_01.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.project_01.R
import com.example.project_01.data.FilmRepository
import com.example.project_01.data.LocalFilmRepository
import com.example.project_01.model.Category
import com.example.project_01.model.Film
import com.example.project_01.model.Status
import java.time.LocalDate


data class AddFilmState(
    var title: String = "",
    var category: Category = Category.Film,
    var status: Status = Status.Seen,
    var image: Uri? = null,
    var selectedDate: LocalDate? = null,
    var rate: Int = -1,
    var comment: String = ""
)

open class AddFilmViewModel : ViewModel(){
    private val repository: FilmRepository = LocalFilmRepository
    private var errorMessage: String = ""
    var state by mutableStateOf(AddFilmState())


    fun loadData() {
        state = AddFilmState()
    }

    fun updateTitle(title: String) {
        state = state.copy(
            title = title
        )
    }

    fun updateCategory(category: Category) {
        state = state.copy(
            category = category
        )
    }

    fun updateStatus(status: Status) {
        state = state.copy(
            status = status
        )
    }

    fun updateImage(uri: Uri?) {
        state = state.copy(
            image = uri
        )
    }

    fun updateDate(date: LocalDate?) {
        state = state.copy(
            selectedDate = date
        )
    }

    fun updateRate(rate: Int) {
        state = state.copy(
            rate = rate
        )
    }

    fun updateComment(comment: String) {
        state = state.copy(
            comment = comment
        )
    }

    fun saveFilm(): Boolean {
        val validated = validateData()
        if (validated) {
            return repository.saveFilm(Film(
                id = generateId(),
                title = state.title,
                category = state.category,
                status = state.status,
                logoString = state.image.toString(),
                logoInt = null,
                releaseDate = state.selectedDate!!,
                rate = if(state.status == Status.Seen) state.rate else -1,
                comment = if(state.status == Status.Seen) state.comment else ""
            ))
        }

        return false
    }

    fun getErrorMessage(): String {
        return errorMessage
    }

    private fun validateData(): Boolean {
        if(state.title.isEmpty()) {
            errorMessage = "Title cannot be empty"
            return false
        }
        else if (state.selectedDate == null || state.selectedDate!!.isAfter(LocalDate.now().plusYears(2))){
            errorMessage = "Selected date is either null or to far in the future"
            return false
        }
        else if(state.status == Status.Seen && state.rate < 0) {
            errorMessage = "Rate is not set"
            return false
        }
        else if (state.status == Status.Seen && state.comment.isEmpty()) {
            errorMessage = "Comment is invalid"
            return false
        }
        else if(state.image == null) {
            errorMessage = "Image is not set"
            return false
        }

        errorMessage = ""
        return true
    }

    private fun generateId(): Int {
        var index =  repository.films.maxOfOrNull { it.id } ?: 0
        return ++index
    }


}