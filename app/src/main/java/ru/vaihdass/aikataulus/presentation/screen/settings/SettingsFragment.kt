package ru.vaihdass.aikataulus.presentation.screen.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import ru.vaihdass.aikataulus.BuildConfig
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentSettingsBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import javax.inject.Inject

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val viewModel: SettingsViewModel by viewModels { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewActions()
        observeData()
    }

    private fun bindViewActions() {
        with(viewBinding) {
            btnShareSchedule.setOnClickListener {
                viewModel.getExportScheduleIntent(
                    BuildConfig.AIKATAULUS_EXPORT_SCHEDULE_URL,
                    requireActivity().packageManager
                )
            }

            btnChangeCourses.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_organizationFragment)
            }

            btnLogout.setOnClickListener {
                btnLogout.isEnabled = false
                viewModel.createLogoutPageIntent()
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            openShareScheduleIntentFlow.observe { intent ->
                startActivity(
                    Intent.createChooser(intent, getString(R.string.share_schedule_header))
                )
            }

            openLogoutIntentFlow.observe { intent ->
                openLogoutPage(intent)
            }

            logoutSuccessFlow.observe {
                val navController = findNavController()

                val navOptions =
                    NavOptions.Builder()
                        .setPopUpTo(R.id.mainFragment, true)
                        .build()

                navController.navigate(
                    R.id.action_settingsFragment_to_authFragment, null, navOptions
                )
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }

    private val logoutResponse = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.logout()
    }

    private fun openLogoutPage(logoutIntent: Intent) {
        logoutResponse.launch(logoutIntent)
    }
}