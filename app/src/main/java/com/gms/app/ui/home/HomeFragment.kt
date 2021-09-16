package com.gms.app.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.gms.app.R
import com.gms.app.databinding.HomeFragmentBinding
import com.gms.app.databinding.SignUpFragmentBinding
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navViewHome.setNavigationItemSelectedListener(this)
        setUpToolbar()
        onViewsClicks()
    }


    private fun onViewsClicks() {
        binding.toolbar.menuImgToolbar.setOnClickListener { openDrawer() }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.home_label)
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