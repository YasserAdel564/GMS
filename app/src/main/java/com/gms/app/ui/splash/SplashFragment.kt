package com.gms.app.ui.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.databinding.SplashFragmentBinding
import com.gms.app.utils.flashAnimation


class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding
    private lateinit var viewModel: SplashVM
    var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        goToHome()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashVM::class.java)
    }

    private fun startAnimation() {
        activity?.flashAnimation(binding.centerImgSplash)
    }

    private fun goToHome() {
        timer = object : CountDownTimer(6000, 500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                findNavController().navigate(
                    R.id.main_fragment, null, NavOptions.Builder().setPopUpTo(
                        R.id.splash_fragment,
                        true
                    ).build()
                )
            }
        }
        timer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }
}