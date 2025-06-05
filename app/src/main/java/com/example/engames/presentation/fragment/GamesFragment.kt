package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.engames.databinding.FragmentGamesBinding
import com.example.engames.interfaces.IRecyclerFragment
import com.example.engames.presentation.adapter.GamesAdapter
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GamesViewModel

class GamesFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
), IRecyclerFragment {
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().showNavigationView()
        setRecyclerLayoutManager()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isTaskReady.observe(viewLifecycleOwner) { isReady ->
            binding.pbLoad.visibility = View.GONE
            if (isReady == true) {
                viewModel.listGames.observe(viewLifecycleOwner) { games ->
                    if (!games.isNullOrEmpty()) {

                    }
                }
                viewModel.isTaskReady.value = false
            }
        }
    }
    override fun setRecyclerViewAdapter() {
        binding.rvGames.adapter = GamesAdapter()
    }
    override fun setRecyclerLayoutManager() {
        binding.rvGames.layoutManager = LinearLayoutManager(context())
    }
}