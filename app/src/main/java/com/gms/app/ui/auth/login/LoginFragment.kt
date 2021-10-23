package com.gms.app.ui.auth.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.databinding.LoginFragmentBinding
import com.gms.app.databinding.SignUpFragmentBinding
import com.gms.app.utils.UiStates
import com.gms.app.utils.isValidEmail
import com.gms.app.utils.observeEvent
import com.gms.app.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginVM by viewModels()
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    override fun onStart() {
        super.onStart()
        preferencesHelper.isLogin = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner, { onLoginResponse(it) })
        setUpToolbar()
        initViewsClicks()
    }

    private fun initViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
        binding.loginBtn.setOnClickListener { validation() }
        binding.signUpBtn.setOnClickListener { goToSignUp() }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.login_label)
    }

    private fun goToSignUp() {
        findNavController().navigate(R.id.action_login_fragment_to_signUp_fragment)
    }

    private fun validation() {
        val email = binding.emailEt.text.trim().toString()
        val password = binding.passwordEt.text.trim().toString()

        if (email.isBlank()) {
            binding.emailEt.error = getString(R.string.empty_email)
            return
        }
        if (!email.isValidEmail()) {
            binding.emailEt.error = getString(R.string.email_validation)
            return
        }
        if (password.isBlank()) {
            binding.passwordEt.error = getString(R.string.empty_password)
            return
        }
//        if (password.length < 8) {
//            binding.passwordEt.error = getString(R.string.password_validation)
//            return
//        }
        viewModel.login(email, password)
    }

    private fun onLoginResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                onSuccess()
            }
            UiStates.Empty -> {
                if (viewModel.infoMessage != null)
                    activity?.snackBar(
                        viewModel.infoMessage,
                        binding.viewRoot
                    )
                viewInputs()
                // goToActivation()
            }
            UiStates.Error -> {
                if (viewModel.infoMessage != null)
                    activity?.snackBar(
                        viewModel.infoMessage,
                        binding.viewRoot
                    )
                viewInputs()
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onLoading() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.viewContainer.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onNoConnection() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        activity?.snackBar(
            requireActivity().resources.getString(R.string.success),
            binding.viewRoot
        )
        viewInputs()
        findNavController().navigate(R.id.action_login_fragment_to_home_fragment)
    }
}