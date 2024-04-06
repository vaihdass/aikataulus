package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.OrganizationEntity
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Organization
import ru.vaihdass.aikataulus.domain.model.OrganizationDomainModel
import javax.inject.Inject

class OrganizationMapper @Inject constructor() {
    fun map(response: List<Organization>?): List<OrganizationDomainModel>? {
        return response?.let {
            it.map { organization -> mapNotNull(organization) }
        }
    }

    fun map(input: Organization?): OrganizationDomainModel? {
        return input?.let { organization -> mapNotNull(organization) }
    }

    fun mapNotNull(organization: Organization): OrganizationDomainModel {
        return OrganizationDomainModel(
            id = organization.id,
            name = organization.name,
        )
    }

    fun map(organizationDomainModel: OrganizationDomainModel?): Organization? {
        return organizationDomainModel?.let { organization ->
            Organization(
                id = organization.id,
                name = organization.name,
            )
        }
    }

    fun mapToDbEntityNotNull(organization: Organization): OrganizationEntity {
        return OrganizationEntity(
            id = organization.id,
            name = organization.name,
        )
    }

    fun mapToDbEntities(response: List<Organization>?): List<OrganizationEntity>? {
        return response?.let {
            it.map { organization -> mapToDbEntityNotNull(organization) }
        }
    }

    fun mapToDbEntity(organization: OrganizationDomainModel) = OrganizationEntity(
        id = organization.id,
        name = organization.name,
    )
}