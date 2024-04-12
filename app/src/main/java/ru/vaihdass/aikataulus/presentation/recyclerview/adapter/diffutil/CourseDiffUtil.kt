package ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel

class CourseDiffUtil(
    private val oldItemsList: List<Pair<CourseUiModel, List<CalendarUiModel>>>,
    private val newItemsList: List<Pair<CourseUiModel, List<CalendarUiModel>>>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return oldItem.first.id == newItem.first.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        val oldCourse = oldItem.first
        val newCourse = newItem.first

        val oldCalendars = oldItem.second
        val newCalendars = newItem.second

        return oldCourse == newCourse && oldCalendars == newCalendars
    }
}