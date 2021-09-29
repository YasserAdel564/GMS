package com.gms.app.ui.main.aboutUs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.databinding.AboutUsFragmentBinding
import com.gms.app.databinding.ProfileFragmentBinding
import com.gms.app.ui.profile.ProfileVM
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import com.gms.app.utils.openBrowser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : Fragment() {

    lateinit var binding: AboutUsFragmentBinding
    private val viewModel: AboutUsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AboutUsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        viewModel.getAboutUsData()
    }


    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                fillViews()
                viewInputs()
            }
            UiStates.Empty -> {
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

    private fun fillViews() {
        val model = viewModel.aboutUsModel
        if (model != null) {
            binding.aboutUsTxt.text =
                HtmlCompat.fromHtml(
                    viewModel.aboutUsModel?.aboutUs.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            binding.sloganTxt.text = model.slogan
            binding.addressTxt.text = model.address
            binding.phoneTxt.text = model.phone
            binding.facebookImg.setOnClickListener { requireActivity().openBrowser(model.facebook) }
            binding.twitterImg.setOnClickListener { requireActivity().openBrowser(model.twitter) }
            binding.instaImg.setOnClickListener { requireActivity().openBrowser(model.google) }
            binding.linkedInImg.setOnClickListener { requireActivity().openBrowser(model.linkedIn) }
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

}