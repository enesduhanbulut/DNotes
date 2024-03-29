package com.duhapp.dnotes.features.note.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.DialogFragmentState
import com.duhapp.dnotes.features.base.ui.showSnippedBottomSheet
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragmentArgs
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIState
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NoteFragment :
    BaseFragment<FragmentNoteBinding, NoteUIEvent, NoteUIState, NoteViewModel>() {
    private var isCategoryShown = false
    override val layoutId = R.layout.fragment_note
    override val titleId = R.string.Note_Fragment
    override val viewModel: NoteViewModel by viewModels()
    override val fragmentTag = "NoteFragment"
    private val categoryViewModel: SelectCategoryViewModel by activityViewModels()
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun initView(binding: FragmentNoteBinding) {
        initBottomSheet()
        viewModel.initState(NoteFragmentArgs.fromBundle(requireArguments()))
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setBackClicked()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        viewModel.initState(NoteFragmentArgs.fromBundle(args))
    }

    private fun initBottomSheet() {
        with(mBinding!!.bottomSheetCategory) {
            bottomSheetBehavior = BottomSheetBehavior.from(this)
            bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        categoryViewModel.onCollapse()
                        viewModel.setEditable(true)
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        hideKeyboard()
                        categoryViewModel.onExpand()
                        viewModel.setEditable(false)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Do something for slide offset.
                }
            }
            bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
            setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }

    private fun collapseBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun handleUIState(it: NoteUIState) {
        when (it) {
            is NoteUIState.Success -> {
                handleSuccessState(it)
            }

            is NoteUIState.Error -> {
                handleErrorState(it)
            }

            else -> {
                Timber.d("State not handled yet $it")
            }
        }
    }

    override fun handleUIEvent(it: NoteUIEvent) {
        when (it) {
            is NoteUIEvent.BackButtonClicked -> {
                viewModel.saveAndGoBackStack()
            }

            is NoteUIEvent.GoToBackStack -> {
                findNavController().popBackStack()
            }

            is NoteUIEvent.ShowWarningDialogBeforeExit -> {
                navigateOptionalDialog(
                    it.customException.data.title,
                    R.string.Note_Could_Not_Be_Saved_Would_You_Like_To_Exit,
                    positiveAction = {
                        viewModel.setEvent(NoteUIEvent.GoToBackStack)
                    },
                    negativeAction = {
                        viewModel.initStateWithLastSuccessState()
                    }
                )
            }

            is NoteUIEvent.CollapseBottomSheet -> {
                collapseBottomSheet()
            }

            else -> {
                Timber.d("Event not handled yet $it")
            }
        }
    }

    private fun handleSuccessState(noteState: NoteUIState.Success) {
        noteState.getSuccessNote().let { note ->
            if (note != null && !isCategoryShown && note.category.id != -1) {
                isCategoryShown = true
                showSnippedBottomSheet(
                    containerId = mBinding!!.bottomSheetContainer.id,
                    fragment = SelectCategoryFragment::class.java,
                    bundle = SelectCategoryFragmentArgs(noteState.getSuccessNote()?.category).toBundle(),
                    activityViewModel = categoryViewModel,
                    eventCollector = { handleBottomSheetEvent(it) },
                    unsubscribeEvent = SelectCategoryUIEvent.Dismiss,
                    stateCollector = { handleBottomSheetState(it) },
                )
            }
        }
    }

    private fun handleErrorState(noteState: NoteUIState.Error) {
        Snackbar.make(
            requireView(),
            noteState.customException.data.message,
            Snackbar.LENGTH_LONG,
        ).show()
    }

    private fun handleBottomSheetEvent(event: SelectCategoryUIEvent) {
        when (event) {
            is SelectCategoryUIEvent.OnCategorySelected -> {
                viewModel.onCategorySelected(event.category)
            }

            else -> {
                Timber.d("Event not handled yet")
            }
        }
    }

    private fun handleBottomSheetState(selectCategoryUIState: SelectCategoryUIState) {
        when (selectCategoryUIState) {
            is SelectCategoryUIState.Error -> {
                viewModel.setState(NoteUIState.Error(selectCategoryUIState.customException))
            }

            else -> {
                Timber.d("State not handled yet $selectCategoryUIState")
            }
        }
    }

    private fun navigateOptionalDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ) {
        findNavController().navigate(
            NoteFragmentDirections.actionNavigationNoteToErrorDialog(
                DialogFragmentState.OptionDialog(
                    title = title,
                    message = message,
                    positiveButtonAction = positiveAction,
                    negativeButtonAction = negativeAction,
                    positiveButtonText = R.string.Yes,
                    negativeButtonText = R.string.No,
                )
            )
        )
    }

    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onPause() {
        viewModel.saveAccordingToLastUIEvent()
        super.onPause()
    }

    override fun onDestroyView() {
        mBinding.let {
            it?.bottomSheetCategory?.setOnClickListener(null)
            it?.bottomSheetCategory?.let { bottomSheet ->
                BottomSheetBehavior.from(bottomSheet).removeBottomSheetCallback(
                    bottomSheetCallback
                )
            }
            it?.bottomSheetCategory?.removeAllViews()
        }
        super.onDestroyView()
    }
}
