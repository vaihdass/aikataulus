package ru.vaihdass.aikataulus.presentation.screen.main

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentMainBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import javax.inject.Inject

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val viewModel: MainViewModel by viewModels { factory.get() }
}