package com.gms.app.ui.contactUs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.databinding.ContactUsFragmentBinding
import com.gms.app.utils.UiStates
import com.gms.app.utils.isValidEmail
import com.gms.app.utils.observeEvent
import com.gms.app.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment : Fragment() {

    private lateinit var binding: ContactUsFragmentBinding
    private val viewModel: ContactUsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactUsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner, { onResponse(it) })
        setUpToolbar()
        initViewsClicks()
    }

    private fun initViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
        binding.sendBtn.setOnClickListener { validation() }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.contact_us_label)
    }

    private fun validation() {
        val name = binding.fullNameEt.text.trim().toString()
        val email = binding.emailEt.text.trim().toString()
        val phone = binding.phoneEt.text.trim().toString()
        val title = binding.titleEt.text.trim().toString()
        val message = binding.messageEt.text.trim().toString()

        if (name.isBlank()) {
            binding.fullNameEt.error = getString(R.string.required_field)
            return
        }

        if (email.isBlank()) {
            binding.emailEt.error = getString(R.string.empty_email)
            return
        }
        if (!email.isValidEmail()) {
            binding.emailEt.error = getString(R.string.email_validation)
            return
        }
        if (phone.isBlank()) {
            binding.phoneEt.error = getString(R.string.required_field)
            return
        }
        if (title.isBlank()) {
            binding.titleEt.error = getString(R.string.required_field)
            return
        }
        if (message.isBlank()) {
            binding.messageEt.error = getString(R.string.required_field)
            return
        }

        viewModel.contactUs(name, email,phone,title,message)
    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                onSuccess()
            }
            UiStates.Empty -> {
                onEmpty()
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

    private fun onEmpty() {
        if (viewModel.infoMessage != null)
            activity?.snackBar(
                viewModel.infoMessage,
                binding.viewRoot
            )
        viewInputs()
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
        if (viewModel.infoMessage != null)
            activity?.snackBar(
                viewModel.infoMessage,
                binding.viewRoot
            )
        viewInputs()
        findNavController().navigateUp()
    }
}