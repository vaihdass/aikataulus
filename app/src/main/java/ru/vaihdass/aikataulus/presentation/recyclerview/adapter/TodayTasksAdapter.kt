package ru.vaihdass.aikataulus.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.ItemTodayTaskBinding
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.diffutil.TaskDiffUtil
import ru.vaihdass.aikataulus.presentation.recyclerview.holder.TodayTaskViewHolder
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class TodayTasksAdapter(
    private val onItemClicked: ((TaskUiModel) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tasks = mutableListOf<TaskUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodayTaskViewHolder(
            viewBinding = ItemTodayTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemClicked = onItemClicked,
        )
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TodayTaskViewHolder)?.bindItem(item = tasks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<TaskUiModel>) {
        val items = newItems.sortedBy { it.subject }.sortedBy { it.isDone }

        val diff = TaskDiffUtil(oldItemsList = tasks, newItemsList = items)
        val diffResult = DiffUtil.calculateDiff(diff)
        tasks.clear()
        tasks.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}