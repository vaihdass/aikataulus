package ru.vaihdass.aikataulus.presentation.screen.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import timber.log.Timber
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
                findNavController().navigate(R.id.action_authFragment_to_greetingFragment)
            }
        }
    }

    private val getAuthResponse = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val dataIntent = it.data ?: return@registerForActivityResult
        handleAuthResponseIntent(dataIntent)
    }

    private fun updateIsLoading(isLoading: Boolean) = with(viewBinding) {
        btnLogin.isVisible = !isLoading
        // loginProgressBar.isVisible = isLoading
    }

    private fun openAuthPage(intent: Intent) {
        getAuthResponse.launch(intent)
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)

        Timber.tag("oauth123").d("exception: %s", exception)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()

        Timber.tag("oauth123").d("token exhange request: %s", tokenExchangeRequest)
        when {
            exception != null -> {
                viewModel.onAuthCodeFailed(exception)
            }

            tokenExchangeRequest != null ->
                viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }
}