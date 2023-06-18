package com.duhapp.dnotes.features.home

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentHomeBinding
import com.duhapp.dnotes.databinding.LayoutHomeCategoryBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.base.ui.BaseListAdapter.OnItemClickListener
import com.duhapp.dnotes.features.generic.ui.ShowMessageBottomSheetViewModel
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUIEvent, HomeUIState, HomeViewModel>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val showMessageBottomSheetViewModel: ShowMessageBottomSheetViewModel by viewModels()
    private lateinit var adapter: BaseListAdapter<HomeCategoryUIModel, LayoutHomeCategoryBinding>
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val titleId: Int
        get() = R.string.title_home

    override fun provideViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun initView(binding: FragmentHomeBinding) {
        adapter = object : BaseListAdapter<HomeCategoryUIModel, LayoutHomeCategoryBinding>() {
            override fun getLayoutId() = R.layout.layout_home_category
            override fun setUIState(binding: LayoutHomeCategoryBinding, item: HomeCategoryUIModel) {
                binding.homeCategoryUIModel = item
                val margin = resources.getDimensionPixelSize(R.dimen.twenty)
                binding.items.addItemDecoration(
                    SpacingItemDecorator(
                        SpaceModel(
                            0, 0, 0, margin
                        )
                    )
                )
                binding.adapter = NoteItemListAdapter().apply {
                    onItemClickListener = OnItemClickListener { noteUIModel, _ ->
                        viewModel.onNoteClick(noteUIModel)
                    }
                }
            }
        }
        val margin = resources.getDimensionPixelSize(R.dimen.xl_margin)
        binding.categories.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(
                    0, margin, 0, 0
                )
            )
        )
        binding.adapter = adapter
        viewModel.loadCategories()

    }

    override fun handleUIEvent(it: HomeUIEvent) {
        when (it) {
            is HomeUIEvent.OnNoteClicked -> {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavigationHomeToNavigationNote(
                        it.noteUIModel
                    )
                )
            }

            else -> {

            }
        }
    }

    override fun handleUIState(it: HomeUIState) {
        adapter.setItems(it.categories)
    }
}