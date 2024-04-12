package ru.vaihdass.aikataulus.presentation.model

data class CalendarUiModel(
    var id: Int,
    var url: String,
    var name: String,
    var courseId: Int,
) {
    override fun toString(): String {
        return name.trim()
    }
}