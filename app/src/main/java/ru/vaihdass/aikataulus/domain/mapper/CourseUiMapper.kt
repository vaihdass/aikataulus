package ru.vaihdass.aikataulus.domain.mapper

import ru.vaihdass.aikataulus.domain.model.CourseDomainModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel

fun CourseDomainModel.toUiModel() = CourseUiModel(
    id = this.id,
    name = this.name,
    organizationId = this.organizationId
)

fun List<CourseDomainModel>.toUiModelList() = this.map { it.toUiModel() }

fun CourseUiModel.toDomainModel() = CourseDomainModel(
    id = this.id,
    name = this.name,
    organizationId = this.organizationId
)

fun List<CourseUiModel>.toDomainModelList() = this.map { it.toDomainModel() }