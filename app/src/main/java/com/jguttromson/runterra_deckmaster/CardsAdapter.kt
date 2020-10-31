package com.jguttromson.runterra_deckmaster

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.card_layout.view.cardImage
import kotlinx.android.synthetic.main.simple_card_layout.view.*


class CardsAdapter(val glide: RequestManager, val cardClickCallback: (Card) -> Unit): RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {
    var cards: List<Card> = emptyList()
    var dataLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return if (!dataLoading) {
            if (cards.isEmpty()) {
                CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_card_layout, parent, false), cardClickCallback)
            } else {
                CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_card_layout, parent, false), cardClickCallback)
            }
        } else {
            CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_layout, parent, false), cardClickCallback)
        }
    }

    override fun getItemCount() = if (cards.isEmpty() || dataLoading) 1 else cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        if (cards.isEmpty() || dataLoading) {
            holder.bind(null, glide)
        } else {
            holder.bind(cards[position], glide)
        }
    }

    class CardViewHolder(val cardView: View, val cardClickCallback: (Card) -> Unit) : RecyclerView.ViewHolder(cardView) {

        fun bind(card: Card?, glide: RequestManager) {
            card?.let {
                cardView.cardBackgroundLoading.setCardBackgroundColor(card.getRegionColor(cardView.context))
                cardView.cardBackgroundLoading.visibility = View.VISIBLE
                cardView.cardLoading.visibility = View.VISIBLE


                glide.load(it.cardAsset)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object: RequestListener<Drawable> {

                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            cardView.cardLoading.visibility = View.GONE
                            cardView.cardLoadFailed.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            cardView.cardBackgroundLoading.visibility = View.GONE
                            return false
                        }
                    })
                    .into(cardView.cardImage)

                cardView.setOnClickListener {
                    cardClickCallback(card)
                }
            }
        }
    }
}