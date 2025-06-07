package com.example.engames.presentation.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.FullStatistic
import com.example.domain.models.LeaderboardModel
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentStatisticBinding
import com.example.engames.presentation.adapter.LeaderboardAdapter
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.StatisticViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

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
            animateClick(shareStatistic) {
                val bitmap = getBitmapFromView(radarSchema)
                val file = saveBitmapToCache(bitmap)
                shareStatistic(file)
            }
        }
    }

    private fun shareStatistic(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context(),
            "${context().packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context().startActivity(
            Intent.createChooser(intent, getString(R.string.share_statistic))
        )
    }

    private fun saveBitmapToCache(bitmap: Bitmap): File {
        val cachePath = File(context().cacheDir, "images").apply { mkdirs() }
        val file = File(cachePath, "shared_image.png")

        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        return file
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = createBitmap(view.width, view.height)
        view.draw(Canvas(bitmap))
        return bitmap
    }

    override fun setObservers() = with(binding) {
        super.setObservers()

        viewModel.leaderboard.observe(viewLifecycleOwner) {
            pbLoadLeaderboard.visibility = View.GONE
            when (it) {
                is ResponseState.Success -> setupLeaderboard(it.data)
                is ResponseState.Error -> showToast(it.message)
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
        rvLeaderboard.apply {
            layoutManager = LinearLayoutManager(context())
            adapter = LeaderboardAdapter(list, context())
        }
    }

    private fun setupRadar(stat: FullStatistic) = with(binding) {
        val maxVal = max(
            stat.quiz_score,
            max(stat.game1_rating, max(stat.game2_rating, max(stat.game3_rating, stat.game4_rating)))
        )
        val maxNum = ((maxVal * 1.2).roundToInt() + 9) / 10 * 10

        radarSchema.apply {
            description.isEnabled = false
            webLineWidth = 1.5f
            webColor = ContextCompat.getColor(context(), R.color.mainColor)
            webLineWidthInner = 1.5f
            webColorInner = ContextCompat.getColor(context(), R.color.mainColor)
            webAlpha = 130
            setExtraOffsets(0f, 0f, 0f, 0f)
            minOffset = 0f
            animateXY(1000, 1000, Easing.EaseInOutQuad)
            xAxis.apply {
                textSize = 12f
                textColor = ContextCompat.getColor(context(), R.color.mainColor)
                valueFormatter = IndexAxisValueFormatter(
                    listOf("Translate", "Write", "Words", "Extra", "Quiz")
                )
            }
            yAxis.apply {
                axisMinimum = 0f
                axisMaximum = maxNum.toFloat()
                setLabelCount(4, true)
                isGranularityEnabled = true
                textSize = 10f
                setDrawLabels(true)
            }
            legend.isEnabled = false
            data = createRadarData(stat)
            invalidate()
        }
        shareStatistic.isClickable = true
    }

    private fun createRadarData(stat: FullStatistic): RadarData {
        val entries = listOf(
            RadarEntry(stat.game1_rating.toFloat()),
            RadarEntry(stat.game2_rating.toFloat()),
            RadarEntry(stat.game3_rating.toFloat()),
            RadarEntry(stat.game4_rating.toFloat()),
            RadarEntry(stat.quiz_score.toFloat())
        )
        val red = ContextCompat.getColor(context(), R.color.red)
        val dataSet = RadarDataSet(entries, "Points").apply {
            color = red
            fillColor = red
            setDrawFilled(true)
            fillAlpha = 180
            lineWidth = 2f
            valueTextSize = 12f
            valueTextColor = ContextCompat.getColor(context(), R.color.mainColor)
            isDrawHighlightCircleEnabled = true
            setDrawHighlightIndicators(false)
        }
        return RadarData(dataSet).apply {
            setDrawValues(true)
        }
    }
    private fun animateClick(view: View, onEnd: () -> Unit) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { onEnd() }
                    .start()
            }
            .start()
    }
}