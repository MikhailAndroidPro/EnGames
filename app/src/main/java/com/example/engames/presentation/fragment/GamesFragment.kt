package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.GameModel
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentGamesBinding
import com.example.engames.presentation.adapter.GamesAdapter
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GamesViewModel

class GamesFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
), GamesAdapter.OnClickListener {
    override val viewModel: GamesViewModel by viewModels()

    override fun click(position: Int) {
        when(position) {
            1 -> findNavController().navigate(R.id.action_gamesFragment_to_gameConnectFragment)
            2 -> findNavController().navigate(R.id.action_gamesFragment_to_gameEnterFragment)
            4 -> findNavController().navigate(R.id.action_gamesFragment_to_gameVictorineFragment)
            else -> {
                val bundle = bundleOf("gamePosition" to position)
                findNavController().navigate(R.id.action_gamesFragment_to_gameChoiceFragment, bundle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadGames()
        setRecyclerLayoutManager()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.listGames.observe(viewLifecycleOwner) { task ->
            when (task) {
                is ResponseState.Success -> {
                    binding.pbLoad.visibility = View.GONE
                    if (task.data.isEmpty() == false) {
                        setRecyclerViewAdapter(task.data)
                    } else {
                        emptyListError()
                    }
                }

                is ResponseState.Error -> {
                    showToast(task.message)
                }
            }

        }
    }

    private fun emptyListError() {

    }

    fun setRecyclerViewAdapter(list: List<GameModel>) {
        binding.rvGames.adapter = GamesAdapter(list, this, App.settingsManager.getCurrentLanguage() == "ru")
    }

    fun setRecyclerLayoutManager() {
        binding.rvGames.layoutManager = LinearLayoutManager(context())
    }
}