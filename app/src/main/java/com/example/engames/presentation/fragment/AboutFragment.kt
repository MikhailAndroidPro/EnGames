package com.example.engames.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.engames.R
import com.example.engames.databinding.FragmentAboutBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import androidx.core.net.toUri

/**
 * Fragment for displaying "About" information and providing a "Write to us" functionality.
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>(
    FragmentAboutBinding::inflate
) {
    /**
     * Sets up click listeners for the "Write to us" text.
     */
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            writeUsTxt.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = StringBuilder(resources.getString(R.string.creator_email))
                        .append("?subject=${resources.getString(R.string.engames_issue)}")
                        .toString().toUri()
                    putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.engames_issue))
                }

                if (emailIntent.resolveActivity(it.context.packageManager) != null) {
                    it.context.startActivity(emailIntent)
                }
            }
        }
    }
}