package ru.vaihdass.aikataulus.presentation.recyclerview.holder

import android.R
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.databinding.ItemCourseBinding
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel

class CoursesViewHolder(
    private val viewBinding: ItemCourseBinding,
    private val onItemClicked: ((CourseUiModel, CalendarUiModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: Pair<CourseUiModel, List<CalendarUiModel>>? = null
    private var calendarsAdapter: ArrayAdapter<CalendarUiModel>? = null

    init {
        viewBinding.actvCalendars.setOnItemClickListener { _, _, position, _ ->
            val calendar = calendarsAdapter?.getItem(position)
            item?.let { item ->
                calendar?.let { calendar ->
                    onItemClicked.invoke(item.first, calendar)
                }
            }
        }
    }

    fun bindItem(item: Pair<CourseUiModel, List<CalendarUiModel>>) {
        this.item = item

        viewBinding.actvCalendars.setHint(item.first.name)

        calendarsAdapter?.let {
            it.clear()
            it.addAll(item.second)
        }

        if (calendarsAdapter == null) {
            calendarsAdapter = ArrayAdapter(itemView.context, R.layout.simple_dropdown_item_1line, item.second)
            viewBinding.actvCalendars.setAdapter(calendarsAdapter)
        }
    }
}