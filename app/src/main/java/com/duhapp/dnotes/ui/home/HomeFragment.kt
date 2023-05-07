package com.duhapp.dnotes.ui.home

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.base.ui.BaseFragment
import com.duhapp.dnotes.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUIEvent, HomeUIState, HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun provideViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun initView(binding: FragmentHomeBinding) {
        TODO("Not yet implemented")
    }

    override fun handleUIEvent(it: HomeUIEvent) {
        TODO("Not yet implemented")
    }
}