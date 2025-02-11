package com.ggaebiz.ggaebiz.data.datastore

import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataStoreParser {

    fun parseJsonToMap(json: String): Map<CharacterName, Map<Int, List<String>>> {
        val jsonObject = Gson().fromJson<Map<String, Map<String, List<String>>>>(
            json, object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
        )

        return jsonObject.mapNotNull { (key, levels) ->
            val characterName = CharacterName.fromKey(key) ?: return@mapNotNull null
            val levelMap = levels.mapKeys { it.key.removePrefix("level").toInt() }
            characterName to levelMap
        }.toMap()
    }

}
