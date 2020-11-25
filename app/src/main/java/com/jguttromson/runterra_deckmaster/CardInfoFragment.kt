package com.jguttromson.runterra_deckmaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jguttromson.runterra_deckmaster.databinding.CardInfoBinding

class CardInfoFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cardId = arguments?.getString("cardId")

        val binding = CardInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.card = Cards.cards?.get(cardId)
        return binding.root
    }
}