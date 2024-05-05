package ru.vaihdass.aikataulus.presentation.screen.event

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentEventBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.helper.TimeFormatHelper
import javax.inject.Inject

class EventFragment : BaseFragment(R.layout.fragment_event) {
    private val viewBinding by viewBinding(FragmentEventBinding::bind)
    private val args: EventFragmentArgs by navArgs()

    @Inject
    lateinit var timeFormatHelper: TimeFormatHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView()
    }

    private fun bindView() {
        val event = args.event

        with(viewBinding) {
            tvSubject.text = event.subject.trim().lowercase().replaceFirstChar { it.titlecase() }

            if (event.location == null) {
                tvLocation.isVisible = false
            } else {
                tvLocation.text = event.location
            }

            tvDate.text = timeFormatHelper.formatDayTimePair(event.dateFrom, event.dateTo)

            var text = ""
            event.type?.let {
                text += getString(R.string.event_type) + it.trim().lowercase().replaceFirstChar { char -> char.titlecase() } + "\n"
            }
            event.teacher?.let {
                text += getString(R.string.event_teacher) + it.trim()
            }

            tvEventInfo.text = text.trim()

            layoutEventInfo.isVisible = text.trim().isEmpty().not()
        }
    }
}