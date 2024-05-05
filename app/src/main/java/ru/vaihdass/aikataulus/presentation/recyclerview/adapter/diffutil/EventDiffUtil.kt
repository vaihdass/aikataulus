package ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.vaihdass.aikataulus.presentation.model.EventUiModel

class EventDiffUtil(
    private val oldItemsList: List<EventUiModel>,
    private val newItemsList: List<EventUiModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return oldItem == newItem
    }
}