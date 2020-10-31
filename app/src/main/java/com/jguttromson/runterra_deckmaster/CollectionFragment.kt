package com.jguttromson.runterra_deckmaster

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.jguttromson.runterra_deckmaster.databinding.CollectionLayoutBinding
import kotlinx.android.synthetic.main.collection_layout.*

class CollectionFragment: Fragment() {

    var contentView: View? = null
    var collectionViewModel: CollectionViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<CollectionLayoutBinding>(inflater, R.layout.collection_layout, container, false)
        binding.lifecycleOwner = this
        contentView = binding.root
        collectionViewModel = ViewModelProviders.of(this)[CollectionViewModel::class.java]
        binding.model = collectionViewModel
        return contentView
    }

    override fun onStart() {
        super.onStart()
        collectionRecyclerView.adapter = CardsAdapter(Glide.with(this)) {
            CardActivity.card = it
            startActivity(Intent(context, CardActivity::class.java))
        }
        collectionRecyclerView.layoutManager = GridLayoutManager(context, 2)
        collectionViewModel?.loadCards(resources)
    }
}