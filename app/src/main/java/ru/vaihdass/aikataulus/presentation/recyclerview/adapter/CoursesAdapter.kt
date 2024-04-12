package ru.vaihdass.aikataulus.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.databinding.ItemCourseBinding
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil.CourseDiffUtil
import ru.vaihdass.aikataulus.presentation.recyclerview.holder.CoursesViewHolder

class CoursesAdapter(
    private val onItemClicked: ((CourseUiModel, CalendarUiModel) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var coursesWithCalendars = mutableListOf<Pair<CourseUiModel, List<CalendarUiModel>>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoursesViewHolder(
            viewBinding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemClicked = onItemClicked
        )
    }

    override fun getItemCount(): Int = coursesWithCalendars.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CoursesViewHolder)?.bindItem(item = coursesWithCalendars[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<Pair<CourseUiModel, List<CalendarUiModel>>>) {
        val diff = CourseDiffUtil(oldItemsList = coursesWithCalendars, newItemsList = newItems)
        val diffResult = DiffUtil.calculateDiff(diff)
        coursesWithCalendars.clear()
        coursesWithCalendars.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}