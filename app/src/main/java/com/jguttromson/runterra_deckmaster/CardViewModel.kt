package com.jguttromson.runterra_deckmaster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel: ViewModel() {

    private var _card: MutableLiveData<Card?> = MutableLiveData<Card?>().apply { value = CardActivity.card }
    public val card: LiveData<Card?>
        get() {
            return _card
        }

    fun setup(card: Card) {
        _card.postValue(card)
    }
}