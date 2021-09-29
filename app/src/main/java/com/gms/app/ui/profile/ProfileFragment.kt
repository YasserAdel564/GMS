package com.gms.app.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.profile.UserDataModel
import com.gms.app.databinding.ProfileFragmentBinding
import com.gms.app.ui.main.home.HomeVM
import com.gms.app.utils.Constants
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: ProfileFragmentBinding
    private val viewModel: ProfileVM by viewModels()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        setUpToolbar()
        setUpViewsClicks()
        viewModel.getUserData()
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.profile_label)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
    }

    private fun fillData() {
        val model = viewModel.userDataModel
        if (model != null) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_user_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)

            Glide.with(requireActivity()).load(Constants.ImagesUrl + model.phone)
                .apply(options)
                .into(binding.profileImg)
            binding.userNameEt.setText(model.fullName)
            binding.passwordEt.setText(model.password)
            binding.addressEt.setText(model.address)
            binding.userEmailEt.setText(model.email)
            binding.phoneEt.setText(model.phone)
            binding.dateOfBirthEt.setText(model.dateOfBirth)
            binding.livingEt.setText(model.living)
            binding.countryEt.setText(model.country)
        }

    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                fillData()
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