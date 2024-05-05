package ru.vaihdass.aikataulus.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.databinding.ItemTodayEventBinding
import ru.vaihdass.aikataulus.presentation.model.EventUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil.EventDiffUtil
import ru.vaihdass.aikataulus.presentation.recyclerview.holder.TodayEventViewHolder

class TodayEventsAdapter(
    private val onItemClicked: ((EventUiModel) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var events = mutableListOf<EventUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodayEventViewHolder(
            viewBinding = ItemTodayEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemClicked = onItemClicked
        )
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TodayEventViewHolder)?.bindItem(item = events[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<EventUiModel>) {
        val diff = EventDiffUtil(oldItemsList = events, newItemsList = newItems)
        val diffResult = DiffUtil.calculateDiff(diff)
        events.clear()
        events.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}