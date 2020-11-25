package com.jguttromson.runterra_deckmaster

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.jguttromson.runterra_deckmaster.databinding.CardDetailLayoutBinding
import kotlinx.android.synthetic.main.card_detail_layout.*

class CardActivity: FragmentActivity() {

    lateinit var cardViewModel: CardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<CardDetailLayoutBinding>(this, R.layout.card_detail_layout)
        cardViewModel = ViewModelProvider(this)[CardViewModel::class.java]
        card?.let {
            binding.model = cardViewModel
        }

        cardImageBack.setOnClickListener {
            onBackPressed()
        }

        cardsViewPager.adapter = CardPagerAdapter(this)
        cardsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val card = if (position == 0) {
                    Cards.cards?.get(card?.cardCode)
                } else {
                    Cards.cards?.get(card?.assocatedCards?.get(position - 1))
                }
                card?.let {
                    loadImages(it)
                }
            }
        })

        TabLayoutMediator(tab_layout, cardsViewPager) { tab, position ->

        }.attach()

        if (card?.assocatedCards.isNullOrEmpty()) {
            tab_layout.visibility = View.GONE
        } else {
            cardsViewPager.layoutParams.apply {
                this as LinearLayout.LayoutParams
                topMargin = 8.dp
            }
        }
    }

    fun loadImages(card: Card) {
        Glide.with(this)
            .load(card.cardFullAsset)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(cardImage)

        Glide.with(this)
            .load(card.cardAsset)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(cardSmallImage)
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

    private inner class CardPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = (card?.assocatedCards?.size ?: 0) + 1

        override fun createFragment(position: Int): Fragment {
            val cardFragment = CardInfoFragment()
            cardFragment.arguments = Bundle().apply {
                putString("cardId", if (position == 0) {
                    card?.cardCode
                } else {
                    card?.assocatedCards?.get(position - 1)
                })
            }
            return cardFragment
        }
    }
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

