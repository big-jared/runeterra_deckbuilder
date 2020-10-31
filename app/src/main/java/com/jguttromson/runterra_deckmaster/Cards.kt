package com.jguttromson.runterra_deckmaster

import android.content.res.Resources
import org.json.JSONArray

object Cards {
    var cards: List<Card>? = null

    fun getCards(resources: Resources, completion: (List<Card>) -> Unit) {
        cards?.let {
            completion(it)
        } ?: run {
            val json = JSONArray(loadData("set1-en_us.json", resources))
            var cardList = mutableListOf<Card>()

            for (i in 0 until json.length()) {
                cardList.add(Card(json.getJSONObject(i)))
            }

            val set2Json = JSONArray(loadData("set2-en_us.json", resources))

            for (i in 0 until set2Json.length()) {
                cardList.add(Card(set2Json.getJSONObject(i)))
            }

            completion(cardList)
        }
    }
}