package com.jguttromson.runterra_deckmaster

import android.content.res.Resources
import org.json.JSONArray

object Cards {
    var cards: MutableMap<String, Card>? = null

    fun getCards(resources: Resources, completion: (List<Card>) -> Unit) {
        cards?.let {
            completion(it.values.toList())
        } ?: run {
            val json = JSONArray(loadData("set1-en_us.json", resources))
            cards = mutableMapOf()

            for (i in 0 until json.length()) {
                val card = Card(json.getJSONObject(i))
                cards?.put(card.cardCode, card)
            }

            val set2Json = JSONArray(loadData("set2-en_us.json", resources))

            for (i in 0 until set2Json.length()) {
                val card = Card(set2Json.getJSONObject(i))
                cards?.put(card.cardCode, card)
            }

            completion(cards?.values?.toList() ?: emptyList())
        }
    }
}