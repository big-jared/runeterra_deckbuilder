package com.jguttromson.runterra_deckmaster

import android.content.Context
import android.graphics.Color
import org.json.JSONArray
import org.json.JSONObject

enum class Region {
    Ionia, Demacia, Zaun, Noxus, ShadowIsles, Bilgewater, Freljord, Error
}

enum class Keyword {
    Challenger, Regeneration, Elusive, Attune, Overwhelm, Burst, Slow, Barrier, Scout, Fast, Fearsome, Deep, Lifesteal,
    Tough, DoubleStrike, LastBreath, Immobile, Vulnerable, QuickStrike, Imbue, CantBlock, Skill, Fleeting, Ephemeral
}

enum class Rarity {
    Champion, Common, None, Rare, Epic
}

enum class Type {
    Spell, Unit, Ability, Trap
}

open class Card(jsonObject: JSONObject) {

    val assocatedCards = jsonObject.getJSONArray("associatedCardRefs")
    val cardAsset = jsonObject.getJSONArray("assets").getJSONObject(0).getString("gameAbsolutePath").replace("http://", "https://")
    val cardFullAsset = jsonObject.getJSONArray("assets").getJSONObject(0).getString("fullAbsolutePath").replace("http://", "https://")
    val region = parseRegion(jsonObject.getString("regionRef"))
    val attack = jsonObject.getInt("attack")
    val defense = jsonObject.getInt("health")
    val cost = jsonObject.getInt("cost")
    val description = jsonObject.getString("descriptionRaw")
    val levelupDescription = jsonObject.getString("levelupDescription")
    val flavorText = jsonObject.getString("flavorText")
    val artistName = jsonObject.getString("artistName")
    val name = jsonObject.getString("name")
    val cardCode = jsonObject.getString("cardCode")
    val keywords = parseKeywords(jsonObject.getJSONArray("keywordRefs"))
    val spellSpeed = jsonObject.getString("spellSpeedRef")
    val rarity = Rarity.valueOf(jsonObject.getString("rarityRef"))
    val subType = jsonObject.getString("subtype")
    val type = Type.valueOf(jsonObject.getString("type"))
    val collectible = jsonObject.getString("collectible") == "true"

    fun parseRegion(region: String) = when (region) {
        "Ionia" -> Region.Ionia
        "Demacia" -> Region.Demacia
        "Noxus" -> Region.Noxus
        "PiltoverZaun" -> Region.Zaun
        "Freljord" -> Region.Freljord
        "ShadowIsles" -> Region.ShadowIsles
        "Bilgewater" -> Region.Bilgewater
        else -> Region.Error
    }

    fun parseKeywords(keywords: JSONArray): List<String> {
        val keywordList = mutableListOf<String>()
        for(i in 0 until keywords.length()) {
            val keywordString = keywords.getString(i)
            if (keywordString.isNotBlank()) {
                keywordList.add(keywordString)
            }
        }

        return keywordList
    }

    fun getRegionColor(context: Context) = when(region) {
        Region.Demacia -> context.resources.getColor(R.color.demacia)
        Region.Bilgewater -> context.resources.getColor(R.color.bilgewater)
        Region.Zaun -> context.resources.getColor(R.color.PiltoverZaun)
        Region.Freljord -> context.resources.getColor(R.color.Freljord)
        Region.Noxus -> context.resources.getColor(R.color.Noxus)
        Region.Ionia -> context.resources.getColor(R.color.Ionia)
        Region.ShadowIsles -> context.resources.getColor(R.color.ShadowIsles)
        else -> Color.GRAY
    }

    fun getRegionIcon(context: Context) = when(region) {
        Region.Demacia -> context.resources.getDrawable(R.drawable.icon_demacia)
        Region.Bilgewater -> context.resources.getDrawable(R.drawable.icon_bilgewater)
        Region.Zaun -> context.resources.getDrawable(R.drawable.icon_piltoverzaun)
        Region.Freljord -> context.resources.getDrawable(R.drawable.icon_freljord)
        Region.Noxus -> context.resources.getDrawable(R.drawable.icon_noxus)
        Region.Ionia -> context.resources.getDrawable(R.drawable.icon_ionia)
        Region.ShadowIsles -> context.resources.getDrawable(R.drawable.icon_shadowisles)
        else -> context.resources.getDrawable(R.drawable.icon_demacia)
    }

    fun typeIcon(context: Context) = when (type) {
        else -> null
    }
}