package com.example.engames.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.FullStatistic
import com.example.domain.models.LeaderboardModel
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentStatisticBinding
import com.example.engames.presentation.adapter.LeaderboardAdapter
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.StatisticViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticFragment : BaseFragment<FragmentStatisticBinding>(
    FragmentStatisticBinding::inflate
) {
    override val viewModel: StatisticViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserStatistic(context())
        viewModel.getLeaderBoard(context())
    }

    override fun applyClick() = with(binding) {
        super.applyClick()
        shareStatistic.setOnClickListener {

        }
    }

    override fun setObservers() = with(binding) {
        super.setObservers()

        viewModel.leaderboard.observe(viewLifecycleOwner) {
            pbLoadLeaderboard.visibility = View.GONE
            when (it) {
                is ResponseState.Success -> {
                    setupLeaderboard(it.data)
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            pbLoadRadar.visibility = View.GONE
            when (it) {
                is ResponseState.Success -> {
                    radarSchema.visibility = View.VISIBLE
                    setupRadar(it.data)
                }

                is ResponseState.Error -> {
                    radarSchema.visibility = View.GONE
                    showToast(it.message)
                }
            }
        }
    }

    private fun setupLeaderboard(list: List<LeaderboardModel>) = with(binding) {
        rvLeaderboard.layoutManager = LinearLayoutManager(context())
        rvLeaderboard.adapter = LeaderboardAdapter(list, context())
    }

    private fun setupRadar(statistic: FullStatistic) = with(binding) {
        radarSchema.data = createRadarData(statistic)

        radarSchema.apply {
            description.isEnabled = false

            webLineWidth = 1f
            webColor = ContextCompat.getColor(context(), R.color.mainColor)
            webLineWidthInner = 1f
            webColorInner = ContextCompat.getColor(context(), R.color.mainColor)
            webAlpha = 100

            legend.isEnabled = false
            animateXY(1000, 1000)
            invalidate()
        }

        radarSchema.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(
                listOf(
                    "Select translation",
                    "Write it correctly",
                    "Word by word",
                    "Extra word",
                    "Quiz"
                )
            )
            textSize = 14f
            textColor = ContextCompat.getColor(context(), R.color.mainColor)
            yOffset = 0f
            xOffset = 0f
        }

        radarSchema.yAxis.apply {
            axisMinimum = 0f
            axisMaximum = 100f
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawTopYLabelEntry(false)
        }
    }

    fun createRadarData(statistic: FullStatistic): RadarData {
        val entries = listOf(
            RadarEntry(statistic.game1_rating.toFloat()),
            RadarEntry(statistic.game2_rating.toFloat()),
            RadarEntry(statistic.game3_rating.toFloat()),
            RadarEntry(statistic.game4_rating.toFloat()),
            RadarEntry(statistic.quiz_score.toFloat())
        )

        val dataSet = RadarDataSet(entries, null).apply {
            color = ContextCompat.getColor(context(), R.color.red)
            fillColor = ContextCompat.getColor(context(), R.color.red)
            setDrawFilled(true)
            fillAlpha = 180
            lineWidth = 3f
            valueTextSize = 12f
            valueTextColor = ContextCompat.getColor(context(), R.color.mainColor)
            setDrawValues(false)
        }
        return RadarData(dataSet)
    }
}