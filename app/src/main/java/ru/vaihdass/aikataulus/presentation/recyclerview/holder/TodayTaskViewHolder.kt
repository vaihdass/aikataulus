package ru.vaihdass.aikataulus.presentation.recyclerview.holder

import android.graphics.Paint
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.ItemTodayTaskBinding
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel

class TodayTaskViewHolder(
    private val viewBinding: ItemTodayTaskBinding,
    private val onItemClicked: ((TaskUiModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: TaskUiModel? = null

    init {
        viewBinding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
    }

    fun bindItem(item: TaskUiModel) {
        this.item = item

        with(viewBinding) {
            tvSubject.text = item.subject.trim().lowercase().replaceFirstChar { it.titlecase() }

            val text = item.task.trim()
            tvTask.text = text
            tvTask.isGone = text.isEmpty()

            if (item.isDone) {
                root.setBackgroundResource(R.drawable.done_task_bg_shape)
                tvSubject.paintFlags = tvSubject.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvSubject.paintFlags = tvSubject.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}
