package ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel

class TaskDiffUtil(
    private val oldItemsList: List<TaskUiModel>,
    private val newItemsList: List<TaskUiModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return oldItem == newItem
    }
}