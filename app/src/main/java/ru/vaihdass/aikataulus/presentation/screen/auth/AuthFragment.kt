package ru.vaihdass.aikataulus.presentation.screen.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentAuthBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.launchAndCollectIn
import ru.vaihdass.aikataulus.presentation.base.toastLong
import javax.inject.Inject

class AuthFragment : BaseFragment(R.layout.fragment_auth) {
    private val viewBinding by viewBinding(FragmentAuthBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val viewModel: AuthViewModel by viewModels { factory.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isAuthorized()) {
            viewBinding.fragmentLayoutAuth.isVisible = false
            navigateAuthorized()
        }

        with(viewBinding) {
            btnLogin.setOnClickListener {
                viewModel.openLoginPage()
            }
        }

        with (viewModel) {
            loadingFlow.launchAndCollectIn(viewLifecycleOwner) { isLoading ->
                updateIsLoading(isLoading)
            }

            openAuthPageFlow.launchAndCollectIn(viewLifecycleOwner) {openAuthPageIntent ->
                openAuthPage(openAuthPageIntent)
            }

            toastFlow.launchAndCollectIn(viewLifecycleOwner) {stringResOfMsg ->
                toastLong(stringResOfMsg)
            }

            authSuccessFlow.launchAndCollectIn(viewLifecycleOwner) {
                navigateAuthorized()
            }
        }
    }

    private val getAuthResponse = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val dataIntent = it.data ?: return@registerForActivityResult
        handleAuthResponseIntent(dataIntent)
    }

    private fun updateIsLoading(isLoading: Boolean) = with(viewBinding) {
        btnLogin.isVisible = !isLoading
    }

    private fun openAuthPage(intent: Intent) {
        getAuthResponse.launch(intent)
    }

    private fun navigateAuthorized() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment, true)
            .build()

        if (viewModel.choseCalendars().not()) {
            findNavController().navigate(
                R.id.action_authFragment_to_greetingFragment,
                null,
                navOptions
            )
        } else findNavController().navigate(
            R.id.action_authFragment_to_mainFragment,
            null,
            navOptions
        )
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)

        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()

        when {
            exception != null -> {
                viewModel.onAuthCodeFailed(exception)
            }

            tokenExchangeRequest != null ->
                viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }
}