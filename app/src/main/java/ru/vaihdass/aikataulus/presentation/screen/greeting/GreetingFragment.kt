package ru.vaihdass.aikataulus.presentation.screen.greeting

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentGreetingBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import javax.inject.Inject

class GreetingFragment : BaseFragment(R.layout.fragment_greeting) {
    private val viewBinding by viewBinding(FragmentGreetingBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val viewModel: GreetingViewModel by viewModels { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        with(viewBinding) {
             btnGet.setOnClickListener { viewModel.fetchTaskLists() }
        }
    }

    private fun observeData() {
        with(viewModel) {
            stateFlow.observe { list ->
                viewBinding.tvTempOutput.text = list.toString()
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }
}