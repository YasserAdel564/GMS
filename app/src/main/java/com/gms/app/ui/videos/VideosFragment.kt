package com.gms.app.ui.videos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import com.gms.app.data.storage.remote.model.video.VideoModel
import com.gms.app.databinding.MyProgrammesFragmentBinding
import com.gms.app.databinding.VideosFragmentBinding
import com.gms.app.ui.myProgrammes.MyProgrammesAdapter
import com.gms.app.ui.myProgrammes.MyProgrammesVM
import com.gms.app.ui.player.ExoPlayer
import com.gms.app.utils.UiStates
import com.gms.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment : Fragment(), VideosAdapter.VideoCallback {

    lateinit var binding: VideosFragmentBinding
    private val viewModel: VideosVM by viewModels()
    private var adapter: VideosAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideosFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observeEvent(viewLifecycleOwner) { onResponse(it) }
        viewModel.getVideos()
        setUpRV()
        setUpToolbar()
        setUpViewsClicks()
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.videos_label)
    }

    private fun setUpViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setUpRV() {
        adapter = VideosAdapter(requireActivity(), this)
        binding.videosRv.adapter = adapter
    }

    private fun onResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                adapter?.renderData(viewModel.videosList)
                viewInputs()
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
        binding.videosRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.videosRv.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onEmpty() {
        binding.videosRv.visibility = View.GONE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.empty.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.videosRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
    }


    private fun onNoConnection() {
        binding.videosRv.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }

    override fun videoClicked(model: VideoModel?) {
        openPlayer(model?.link.toString())
    }

    private fun openPlayer(url: String) {
        ExoPlayer.Start.start(requireActivity(), url)
    }

}