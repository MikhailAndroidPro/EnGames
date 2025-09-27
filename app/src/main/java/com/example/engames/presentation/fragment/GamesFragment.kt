package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.GameModel
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.DialogRateAppBinding
import com.example.engames.databinding.FragmentGamesBinding
import com.example.engames.presentation.adapter.GamesAdapter
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GamesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Fragment to display a list of games.
class GamesFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
), GamesAdapter.OnClickListener {
    override val viewModel: GamesViewModel by viewModels()

    // Handles game item click and navigates to the corresponding game fragment.
    override fun click(position: Int) {
        when(position) {
            1 -> findNavController().navigate(R.id.action_gamesFragment_to_gameEnterFragment)
            2 -> findNavController().navigate(R.id.action_gamesFragment_to_gameConnectFragment)
            4 -> findNavController().navigate(R.id.action_gamesFragment_to_gameVictorineFragment)
            else -> {
                val bundle = bundleOf("gamePosition" to position)
                findNavController().navigate(R.id.action_gamesFragment_to_gameChoiceFragment, bundle)
            }
        }
    }

    // Called when the view is created, loads games and sets up RecyclerView layout.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadGames()
        setRecyclerLayoutManager()

        if (!App.sharedManager.isFirstOpen()) showRateUsDialog()
        else App.sharedManager.endFirstOpen()
    }

    // Observes LiveData for game list updates and handles success/error states.
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

    // Placeholder function to handle empty game list error (currently does nothing).
    private fun emptyListError() {
        showToast(getString(R.string.the_game_list_could_t_be_loaded))
    }

    // Sets the RecyclerView adapter with the list of games.
    fun setRecyclerViewAdapter(list: List<GameModel>) {
        binding.rvGames.adapter = GamesAdapter(list.sortedBy { it.id }, this, App.settingsManager.getCurrentLanguage() == "ru")
    }

    // Sets the RecyclerView layout manager.
    fun setRecyclerLayoutManager() {
        binding.rvGames.layoutManager = LinearLayoutManager(context())
    }

    // Shows dialog to rate the app
    private fun showRateUsDialog() {
        if (App.sharedManager.getUserRateUs() > 0f) return

        val binding = DialogRateAppBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(context(), R.style.RateDialogTheme)
            .setTitle(R.string.rate_us)
            .setMessage(R.string.your_opinion_is_important)
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.ok), null)
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .create()

        dialog.setOnShowListener {
            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            btnNegative?.setTextColor(context().getColor(R.color.red))
            btnPositive.isEnabled = false

            binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                btnPositive.isEnabled = rating > 0f
            }

            btnPositive.setOnClickListener {
                val rating = binding.ratingBar.rating
                App.sharedManager.setUserRate(rating)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}