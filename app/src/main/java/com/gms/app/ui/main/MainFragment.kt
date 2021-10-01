package com.gms.app.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.awesomedialog.*
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.databinding.MainFragmentBinding
import com.gms.app.ui.auth.signUp.SignUpVM
import com.gms.app.ui.main.home.HomeVM
import com.gms.app.ui.profile.ProfileVM
import com.gms.app.utils.Constants
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainVM by viewModels()
    private val profileVM: ProfileVM by viewModels()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileVM.uiState.observeEvent(viewLifecycleOwner, { renderUserData(it) })
        binding.navViewHome.setNavigationItemSelectedListener(this)
        initBottomNavigation()
        setUpToolbar(requireActivity().resources.getString(R.string.app_name))
        onViewsClicks()
        setUpNavDrawer()
        //   onBottomNavigationClicked()
    }

    private fun setUpNavDrawer() {
        val img = binding.navViewHome.getHeaderView(0).findViewById<ImageView>(R.id.username_nav_header_img)
        val text = binding.navViewHome.getHeaderView(0).findViewById<TextView>(R.id.username_nav_header_tv)
        if (preferencesHelper.isLogin) {
            profileVM.getUserData()
            binding.navViewHome.menu.findItem(R.id.auth_fragment).title =
                requireActivity().getString(R.string.logout)
            binding.navViewHome.menu.findItem(R.id.profile_fragment).isVisible = true
        } else {
            Glide.with(requireActivity()).load(R.drawable.default_user_image).centerInside()
                .into(img)
            text.text = requireActivity().getString(R.string.guest)
            binding.navViewHome.menu.findItem(R.id.auth_fragment).title = requireActivity().getString(R.string.login_label)
            binding.navViewHome.menu.findItem(R.id.profile_fragment).isVisible = false
        }


    }
    private fun showAlertDialog() {
        AwesomeDialog.build(requireActivity())
            .title(requireActivity().resources.getString(R.string.alert))
            .body(requireActivity().resources.getString(R.string.want_to_logout))
            .background(R.drawable.purple_rectangle_dialog_bg)
            .onPositive(
                requireActivity().resources.getString(R.string.ok),
                R.drawable.rectangle_dialog_bg
            )
            { goToLogin() }
            .onNegative(
                requireActivity().resources.getString(R.string.cancel),
                R.drawable.rectangle_dialog_bg
            )

    }
    private fun goToLogin() {
        findNavController().navigate(R.id.action_main_fragment_to_login_fragment)
    }

    private fun initBottomNavigation() {
        if (!viewModel.isOpened) {
            viewModel.isOpened = true
            Navigation.findNavController(requireActivity(), R.id.bottomNavigationHost)
                .navigate(R.id.home_fragment)
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.bottomNavigationHost)
        NavigationUI.setupWithNavController(binding.bottomNavigationHome, navController)
    }

    private fun onBottomNavigationClicked() {
        binding.bottomNavigationHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_fragment -> setUpToolbar(requireActivity().resources.getString(R.string.home_label))
                R.id.programs_fragment -> setUpToolbar(requireActivity().resources.getString(R.string.programs_label))
                R.id.about_us_fragment -> setUpToolbar(requireActivity().resources.getString(R.string.about_us_label))
                R.id.contact_us_fragment -> setUpToolbar(requireActivity().resources.getString(R.string.contact_us_label))
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun onViewsClicks() {
        binding.toolbar.menuImgToolbar.setOnClickListener { openDrawer() }
    }

    private fun setUpToolbar(pageName: String) {
        binding.toolbar.titleTvToolbar.text = pageName

    }

    private fun openDrawer() {
        binding.drawerLayoutHome.openDrawer(GravityCompat.START)
    }

    private fun renderUserData(states: UiStates?) {
        when (states) {
            UiStates.Success -> {
                Glide.with(requireActivity())
                    .load(Constants.ImagesUrl + profileVM.userDataModel?.picture).centerInside()
                    .placeholder(R.drawable.default_user_image)
                    .into(binding.navViewHome.findViewById(R.id.username_nav_header_img))
                binding.navViewHome.findViewById<TextView>(R.id.username_nav_header_tv).text =
                    profileVM.userDataModel?.fullName
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.auth_fragment) {
            if (preferencesHelper.isLogin)
                showAlertDialog()
            else
                goToLogin()
        }
        binding.drawerLayoutHome.close()
        val navController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return (NavigationUI.onNavDestinationSelected(menuItem, navController)
                || super.onOptionsItemSelected(menuItem))
    }

}