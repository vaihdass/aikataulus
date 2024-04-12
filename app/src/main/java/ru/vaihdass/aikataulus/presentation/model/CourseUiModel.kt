package ru.vaihdass.aikataulus.presentation.model

data class CourseUiModel (
    var id: Int,
    var name: String,
    var organizationId: Int,
) {
    override fun toString(): String {
        return name.trim()
    }
}