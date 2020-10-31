package com.jguttromson.runterra_deckmaster

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jguttromson.runterra_deckmaster.databinding.CardDetailLayoutBinding
import kotlinx.android.synthetic.main.card_detail_layout.*

class CardActivity: FragmentActivity() {

    lateinit var cardViewModel: CardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<CardDetailLayoutBinding>(this, R.layout.card_detail_layout)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        card?.let {
            binding.model = cardViewModel
            Glide.with(this)
                .load(it.cardAsset)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(cardImage)
        }

        cardImage?.post {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val peekHeight = displayMetrics.heightPixels - cardImage.height - cardImageBack.height - 150
            val bottomsheetBehavior = BottomSheetBehavior.from(timesheetMapBottomSheet)
            bottomsheetBehavior.isHideable = false
            bottomsheetBehavior.peekHeight = peekHeight
        }
    }

    override fun onResume() {
        super.onResume()
        card?.let {
            cardViewModel.setup(it)
        }
    }

    companion object {
        var card: Card? = null
    }
}