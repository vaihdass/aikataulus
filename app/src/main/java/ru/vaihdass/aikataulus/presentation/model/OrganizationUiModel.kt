package ru.vaihdass.aikataulus.presentation.model

data class OrganizationUiModel (
    var id: Int,
    var name: String,
) {
    override fun toString(): String {
        return name.trim()
    }
}