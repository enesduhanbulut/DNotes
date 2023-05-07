package com.duhapp.dnotes.ui.home

import com.duhapp.dnotes.base.ui.BaseListAdapter
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.base.ui.BaseFragment
import com.duhapp.dnotes.base.ui.NoteItemListAdapter
import com.duhapp.dnotes.databinding.FragmentHomeBinding
import com.duhapp.dnotes.databinding.LayoutHomeCategoryBinding
import com.duhapp.dnotes.home_category.ui.HomeCategoryUIModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUIEvent, HomeUIState, HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: BaseListAdapter<HomeCategoryUIModel, LayoutHomeCategoryBinding>
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun provideViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun initView(binding: FragmentHomeBinding) {
        adapter = object :
            BaseListAdapter<HomeCategoryUIModel, LayoutHomeCategoryBinding>() {
            override fun getLayoutId() = R.layout.layout_home_category
            override fun setUIState(binding: LayoutHomeCategoryBinding, item: HomeCategoryUIModel) {
                binding.homeCategoryUIModel = item
                binding.items.adapter = NoteItemListAdapter()
                (binding.items.adapter as NoteItemListAdapter).setItems(item.noteList)
            }
        }
        binding.categories.adapter = adapter
        viewModel.uiState.value?.let { adapter.setItems(it.categories) }

    }

    override fun handleUIEvent(it: HomeUIEvent) {
        TODO("Not yet implemented")
    }
}