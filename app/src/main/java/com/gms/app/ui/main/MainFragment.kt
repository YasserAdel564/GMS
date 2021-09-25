package com.gms.app.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.gms.app.R
import com.gms.app.databinding.MainFragmentBinding
import com.gms.app.ui.auth.signUp.SignUpVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navViewHome.setNavigationItemSelectedListener(this)
        initBottomNavigation()
        setUpToolbar(requireActivity().resources.getString(R.string.app_name))
        onViewsClicks()
     //   onBottomNavigationClicked()
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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        binding.drawerLayoutHome.close()
        val navController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return (NavigationUI.onNavDestinationSelected(menuItem, navController)
                || super.onOptionsItemSelected(menuItem))
    }

}