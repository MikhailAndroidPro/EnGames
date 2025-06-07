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

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected open val viewModel: BaseViewModel? = null
    protected fun context(): Context = requireContext()
    protected fun activity(): BaseActivity = requireActivity() as BaseActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.resumeState()
        applyClick()
        setObservers()
    }

    protected fun showToast(message: String) {
        Toast.makeText(context(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes resId: Int) {
        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    open fun setObservers() {}
    open fun applyClick() {}
}