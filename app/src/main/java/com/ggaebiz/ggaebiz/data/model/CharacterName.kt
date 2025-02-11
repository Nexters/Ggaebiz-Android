package com.ggaebiz.ggaebiz.data.model


enum class CharacterName(val key: String) {
    KIKI("kiki"),
    BOBO("bobo"),
    NANA("nana"),
    CHACHA("chacha"),
    BOOBOO("booboo");

    companion object {
        fun fromKey(key: String): CharacterName? {
            return entries.find { it.key == key }
        }
    }
}
