package ru.vaihdass.aikataulus.presentation.screen.greeting

import android.content.Context
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent

class GreetingFragment : BaseFragment(R.layout.fragment_greeting) {

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(fragment = this)
        super.onAttach(context)
    }
}