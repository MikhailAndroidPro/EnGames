package com.example.engames.presentation.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.engames.R
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.activity.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Abstract base class for Fragments, handling view binding and common UI functionalities.
 *
 * @param VB The type of ViewBinding associated with this Fragment.
 * @property bindingInflater A lambda function to inflate the ViewBinding.
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    /** Provides access to the ViewBinding instance, non-null after onCreateView. */
    protected val binding get() = _binding!!
    /** Optional ViewModel associated with this Fragment. */
    protected open val viewModel: BaseViewModel? = null
    /** Returns the required Context. */
    protected fun context(): Context = requireContext()
    /** Returns the required BaseActivity. */
    protected fun activity(): BaseActivity = requireActivity() as BaseActivity

    /**
     * Inflates the layout and initializes the ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }
    /**
     * Called after the view is created. Resumes ViewModel state and sets up UI.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.resumeState()
        applyClick()
        setObservers()
    }
    /**
     * Shows a short toast message.
     */
    protected fun showToast(message: String) {
        Toast.makeText(context(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes resId: Int) {
        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
    }
    /**
     * Displays a dialog, typically for end-of-game scenarios.
     */
    protected fun showEndGameDialog(
        title: String,
        message: String,
        onConfirm: () -> Unit,
    ) {
        val dialog = MaterialAlertDialogBuilder(context())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(context().getColor(R.color.red))
    }

    /**
     * Clears the ViewBinding reference when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /** Placeholder for setting LiveData observers. */
    open fun setObservers() {}
    /** Placeholder for setting click listeners. */
    open fun applyClick() {}
}