package com.example.engames.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Generic RecyclerView adapter with ViewBinding.
 * @param T Type of data items.
 * @param VB Type of ViewBinding.
 * @property list Data items.
 * @property bindingInflater Function to inflate ViewBinding.
 */
abstract class BaseRecyclerAdapter<T, VB : ViewBinding>(
    private val list: List<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB
) : RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder<VB>>() {

    /** ViewHolder for the adapter. */
    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    /** Creates a ViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        return BaseViewHolder(bindingInflater(LayoutInflater.from(parent.context), parent, false))
    }

    /** Binds data to a ViewHolder. */
    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        onBind(holder.binding, list[position], position)
    }

    /** Returns the number of items. */
    override fun getItemCount(): Int = list.size
    protected abstract fun onBind(binding: VB, item: T, position: Int)
}