package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.CourseEntity
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Course
import ru.vaihdass.aikataulus.domain.model.CourseDomainModel
import javax.inject.Inject

class CourseMapper @Inject constructor() {
    fun map(response: List<Course>?): List<CourseDomainModel>? {
        return response?.let {
            it.map { course -> mapNotNull(course) }
        }
    }

    fun map(input: Course?): CourseDomainModel? {
        return input?.let { course -> mapNotNull(course) }
    }

    fun mapNotNull(course: Course): CourseDomainModel {
        return CourseDomainModel(
            id = course.id,
            name = course.name,
            organizationId = course.organizationId,
        )
    }

    fun map(courseDomainModel: CourseDomainModel?): Course? {
        return courseDomainModel?.let { course ->
            Course(
                id = course.id,
                name = course.name,
                organizationId = course.organizationId,
            )
        }
    }

    fun mapToDbEntityNotNull(course: Course): CourseEntity {
        return CourseEntity(
            id = course.id,
            name = course.name,
            organizationId = course.organizationId,
        )
    }

    fun mapToDbEntities(response: List<Course>?): List<CourseEntity>? {
        return response?.let {
            it.map { course -> mapToDbEntityNotNull(course) }
        }
    }
}