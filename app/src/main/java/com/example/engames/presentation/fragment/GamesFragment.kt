package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.engames.R
import com.example.engames.databinding.FragmentGamesBinding
import com.example.engames.interfaces.IRecyclerFragment
import com.example.engames.presentation.adapter.GamesAdapter
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GamesViewModel

class GamesFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
), IRecyclerFragment {
    override val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadGames()
        setRecyclerLayoutManager()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isTaskReady.observe(viewLifecycleOwner) { isReady ->
            if (isReady == true) {
                binding.pbLoad.visibility = View.GONE
                if (viewModel.listGames.value?.isEmpty() == false) {
                    setRecyclerViewAdapter()
                }
                viewModel.isTaskReady.value = false
            }
        }
    }

    override fun setRecyclerViewAdapter() {
        binding.rvGames.adapter = GamesAdapter(listOf())
    }

    override fun setRecyclerLayoutManager() {
        binding.rvGames.layoutManager = LinearLayoutManager(context())
    }
}