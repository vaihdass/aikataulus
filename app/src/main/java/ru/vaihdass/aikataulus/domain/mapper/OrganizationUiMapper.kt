package ru.vaihdass.aikataulus.domain.mapper

import ru.vaihdass.aikataulus.domain.model.OrganizationDomainModel
import ru.vaihdass.aikataulus.presentation.model.OrganizationUiModel

fun OrganizationDomainModel.toUiModel() = OrganizationUiModel(
    id = this.id,
    name = this.name
)

fun List<OrganizationDomainModel>.toUiModelList() = this.map { it.toUiModel() }

fun OrganizationUiModel.toDomainModel() = OrganizationDomainModel(
    id = this.id,
    name = this.name
)

fun List<OrganizationUiModel>.toDomainModelList() = this.map { it.toDomainModel() }