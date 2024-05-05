package ru.vaihdass.aikataulus.presentation.screen.greeting

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentCoursesBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.model.OrganizationUiModel
import javax.inject.Inject

class CoursesFragment : BaseFragment(R.layout.fragment_courses) {
    private val viewBinding by viewBinding(FragmentCoursesBinding::bind)
    private val args: CoursesFragmentArgs by navArgs()

    @Inject
    lateinit var factory: CoursesAssistedViewModel.Factory
    private val viewModel: CoursesAssistedViewModel by viewModels {
        CoursesAssistedViewModel.provideFactory(
            assistedFactory = factory,
            organization = OrganizationUiModel(args.organizationId, args.organizationName)
        )
    }

    private var choseCalendars: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            initAdapter { course, calendar ->
                saveSelectedCalendar(course, calendar)
            }

            if (isCalendarsSelected()) clearCalendarsSelected()

            choseCalendars = viewModel.choseCalendars()
        }

        with(viewBinding) {
            rvCourses.adapter = viewModel.coursesAdapter

            srlCourses.setOnRefreshListener {
                srlCourses.isRefreshing = true

                viewModel.fetchCalendars()

                srlCourses.postDelayed({ srlCourses.isRefreshing = false }, 3000)
            }

            btnComplete.setOnClickListener {
                btnComplete.isEnabled = false

                viewModel.saveCalendars()
            }
        }

        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            calendarsFlow.observe { calendars ->
                coursesAdapter.setItems(calendars)

                viewBinding.btnComplete.isEnabled = true
                viewBinding.srlCourses.isRefreshing = false
            }

            saveCalendarsSuccessFlow.observe {
                val navController = findNavController()

                val fragmentId = if (choseCalendars) R.id.mainFragment else R.id.organizationFragment

                val navOptions = NavOptions.Builder()
                        .setPopUpTo(fragmentId, true)
                        .build()

                navController.navigate(
                    R.id.action_coursesFragment_to_mainFragment,
                    null,
                    navOptions
                )
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }
}