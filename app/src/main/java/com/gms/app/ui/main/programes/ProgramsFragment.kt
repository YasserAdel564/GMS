package com.gms.app.ui.main.programes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gms.app.R
import com.gms.app.databinding.HomeFragmentBinding
import com.gms.app.databinding.ProgramsFragmentBinding
import com.gms.app.ui.main.home.DepartmentsAdapter
import com.gms.app.utils.TestList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgramsFragment : Fragment() {

    lateinit var binding: ProgramsFragmentBinding
    private lateinit var viewModel: ProgramsVM
    private var adapter: ProgramsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProgramsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProgramsVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
    }

    private fun setUpRV() {
        adapter = ProgramsAdapter(requireActivity(),
            TestList.ITEMS as ArrayList<TestList.TestItem>
        )
        binding.homeRv.adapter = adapter
    }

}