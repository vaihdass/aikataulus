package ru.vaihdass.aikataulus.presentation.screen.greeting

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentOrganizationBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import javax.inject.Inject

class OrganizationFragment : BaseFragment(R.layout.fragment_organization) {
    private val viewBinding by viewBinding(FragmentOrganizationBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val viewModel: OrganizationViewModel by viewModels { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            viewModel.initAdapter(requireContext())
            actvOrganizations.setAdapter(viewModel.organizationsAdapter)

            actvOrganizations.setOnItemClickListener { _, _, position, _ ->
                val organization = viewModel.organizationsAdapter.getItem(position)

                btnNext.isEnabled = organization != null
                organization?.let {
                    viewModel.selectedOrganization = organization
                }
            }

            btnNext.setOnClickListener {
                viewModel.selectedOrganization?.let {
                    val action = OrganizationFragmentDirections
                        .actionGreetingFragmentToCoursesFragment(
                            organizationId = it.id,
                            organizationName = it.name
                        )

                    findNavController().navigate(action)
                }
            }
        }

        with(viewModel) {
            viewModel.selectedOrganization?.let {
                val position = organizationsAdapter.getPosition(selectedOrganization)
                if (position != -1) {
                    viewBinding.actvOrganizations.setSelection(position)
                    viewBinding.btnNext.isEnabled = true
                }
            }
        }

        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            organizationsFlow.observe { organizations ->
                organizationsAdapter.clear()
                organizationsAdapter.addAll(organizations)
                organizationsAdapter.notifyDataSetChanged()
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }
}