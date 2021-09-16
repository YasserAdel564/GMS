package com.gms.app.ui.auth.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.databinding.LoginFragmentBinding
import com.gms.app.databinding.SignUpFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        initViewsClicks()
    }

    private fun initViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
        binding.signUpBtn.setOnClickListener { goToSignUp() }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.login_label)
    }

    private fun goToSignUp() {
        findNavController().navigate(R.id.action_login_fragment_to_signUp_fragment)
    }
}