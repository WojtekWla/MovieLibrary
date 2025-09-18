package com.example.project_01.model

import java.time.LocalDate

enum class Category {
    Film,
    Series,
    Document,
}

enum class Status {
    Seen,
    Unseen
}

data class Film (
    val id: Int,
    var logoInt: Int?,
    var logoString: String? = null,
    var title: String,
    var releaseDate: LocalDate,
    var category: Category,
    var status: Status,
    var rate: Int = -1,
    var comment: String = ""
)