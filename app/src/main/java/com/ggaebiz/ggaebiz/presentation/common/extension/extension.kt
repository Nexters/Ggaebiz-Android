package com.ggaebiz.ggaebiz.presentation.common.extension

import android.content.Context
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.presentation.model.AlarmCharacterData
import com.ggaebiz.ggaebiz.presentation.model.AlarmCharacterData.Companion.ALARM_CHARACTER_MAP


fun Context.getRawResId(resourceName: String): Int {
    val resName = resourceName.replace("raw/", "")
    return resources.getIdentifier(resName, "raw", packageName)
}

// TODO :: 인덱스 말고 .. enum으로 관리하고 싶습니닷
fun Int.getCharacterData(): AlarmCharacterData? {
    return when (this) {
        0 -> ALARM_CHARACTER_MAP[CharacterName.KIKI]
        1 -> ALARM_CHARACTER_MAP[CharacterName.BOBO]
        2 -> ALARM_CHARACTER_MAP[CharacterName.NANA]
        3 -> ALARM_CHARACTER_MAP[CharacterName.CHACHA]
        4 -> ALARM_CHARACTER_MAP[CharacterName.BOOBOO]
        else -> ALARM_CHARACTER_MAP[CharacterName.KIKI]
    }
}