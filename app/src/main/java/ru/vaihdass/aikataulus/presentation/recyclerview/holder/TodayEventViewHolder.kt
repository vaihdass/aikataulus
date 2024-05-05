package ru.vaihdass.aikataulus.presentation.recyclerview.holder

import androidx.recyclerview.widget.RecyclerView
import ru.vaihdass.aikataulus.base.Constants.AIKATAULUS_UI_EVENT_TIME_PATTERN
import ru.vaihdass.aikataulus.databinding.ItemTodayEventBinding
import ru.vaihdass.aikataulus.presentation.model.EventUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayEventViewHolder(
    private val viewBinding: ItemTodayEventBinding,
    private val onItemClicked: ((EventUiModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: EventUiModel? = null

    init {
        viewBinding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
    }

    fun bindItem(item: EventUiModel) {
        this.item = item

        with(viewBinding) {
            tvSubject.text = item.subject.trim().lowercase().replaceFirstChar { it.titlecase() }
            tvLocation.text = item.location?.trim() ?: ""
            tvTime.text = formatTimeRange(item.dateFrom, item.dateTo)
            tvType.text = (item.type?.trim() ?: "").lowercase().replaceFirstChar { it.titlecase() }
        }
    }

    private fun formatTimeRange(dateFrom: Date, dateTo: Date): String {
        val timeFormat = SimpleDateFormat(AIKATAULUS_UI_EVENT_TIME_PATTERN, Locale.getDefault())
        return "${timeFormat.format(dateFrom)} – ${timeFormat.format(dateTo)}"
    }
}