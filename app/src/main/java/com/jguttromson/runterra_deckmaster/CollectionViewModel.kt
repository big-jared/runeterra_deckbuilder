package com.jguttromson.runterra_deckmaster

import android.content.res.Resources
import android.text.Editable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

enum class FilterType {
    Search, Mana, Keyword, Region, Rarity, Type
}

class CollectionViewModel : ViewModel() {

    private var allCards = mutableListOf<Card>()
    private val _cards = MutableLiveData<List<Card>>().apply { value = emptyList() }
    val cards: LiveData<List<Card>>
        get() { return _cards }

    private val _dataLoading = MutableLiveData<Boolean>().apply { value = true }
    val dataLoading: LiveData<Boolean> get() { return _dataLoading }

    val filters: MutableMap<FilterType, (Card) -> Boolean> = mutableMapOf()

    private val _enabledRegions = MutableLiveData<MutableList<Region>>().apply { mutableListOf<Region>() }
    val enabledRegions: LiveData<MutableList<Region>> get() { return _enabledRegions }

    private val _enabledTypes = MutableLiveData<MutableList<Type>>().apply { mutableListOf<Type>() }
    val enabledTypes: LiveData<MutableList<Type>> get() { return _enabledTypes }

    private val _enabledFilter = MutableLiveData<FilterType>().apply { value = null }
    val enabledFilter: LiveData<FilterType> get() { return _enabledFilter }

    var searchText: String? = null

    fun loadCards(resources: Resources) {
        _dataLoading.postValue(true)
        Thread(Runnable {
            val json = JSONArray(loadData("set1-en_us.json", resources))
            val cardList = mutableListOf<Card>()

            for (i in 0 until json.length()) {
                cardList.add(Card(json.getJSONObject(i)))
            }

            val set2Json = JSONArray(loadData("set2-en_us.json", resources))

            for (i in 0 until set2Json.length()) {
                cardList.add(Card(set2Json.getJSONObject(i)))
            }

            cardList.sortBy { it.cost }
            allCards = cardList
            _cards.postValue(cardList)
            _dataLoading.postValue(false)
        }).start()
    }

    fun search(editable: Editable) {
        searchText = editable.toString()

        filters[FilterType.Search] = { card -> searchText?.let { card.name.contains(it, true) } ?: true }
        applyFilters()
    }

    fun toggleFilterType(filterType: FilterType) {
        val currentValue = _enabledFilter.value
        if (currentValue != null && currentValue == filterType) {
            _enabledFilter.postValue(null)
        } else {
            _enabledFilter.postValue(filterType)
        }
    }

    fun toggleRegion(region: Region) {
        val enabledRegions = _enabledRegions.value ?: mutableListOf()
        if (enabledRegions.contains(region)) {
            enabledRegions.remove(region)
        } else {
            enabledRegions.add(region)
        }

        filters[FilterType.Region] = { card -> enabledRegions.isEmpty() || enabledRegions.contains(card.region) }
        _enabledRegions.postValue(enabledRegions)
        applyFilters()
    }

    fun toggleType(type: Type) {
        val enabledTypes = _enabledTypes.value ?: mutableListOf()
        if (enabledTypes.contains(type)) {
            enabledTypes.remove(type)
        } else {
            enabledTypes.add(type)
        }

        filters[FilterType.Type] = { card -> enabledTypes.isEmpty() || enabledTypes.contains(card.type) }
        _enabledTypes.postValue(enabledTypes)
        applyFilters()
    }

    fun applyFilters() {
        Thread(Runnable {
            val filteredCards = allCards.filter { card -> filters.all { it.value(card) } }
            _cards.postValue(filteredCards)
        }).start()
    }

    fun clearFilters() {
        filters.clear()
        _enabledRegions.postValue(mutableListOf())
        applyFilters()
    }

    fun activeRegionsText() = enabledRegions.value?.let {
        if (it.isNotEmpty()) {
            "(${it.size})"
        } else {
            ""
        }
    } ?: ""

    fun activeTypesText() = enabledTypes.value?.let {
        if (it.isNotEmpty()) {
            "(${it.size})"
        } else {
            ""
        }
    } ?: ""

    companion object {
        @BindingAdapter("cards")
        @JvmStatic
        fun RecyclerView.setCards(cards: List<Card>) {
            adapter?.let {
                if (it is CardsAdapter) {
                    it.cards = cards
                    it.notifyDataSetChanged()
                }
            }
        }

        @BindingAdapter("dataLoading")
        @JvmStatic
        fun RecyclerView.setDataLoading(dataLoading: Boolean) {
            adapter?.let {
                if (it is CardsAdapter) {
                    it.dataLoading = dataLoading
                    removeAllViews()
                    it.notifyDataSetChanged()
                }
            }
        }

        @BindingAdapter("android:afterTextChanged")
        @JvmStatic
        fun setListener(view: TextView?, after: AfterTextChanged?) {
            setListener(view, after)
        }
    }
}