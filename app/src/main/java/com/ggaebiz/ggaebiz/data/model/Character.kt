package com.ggaebiz.ggaebiz.data.model


enum class Character(val key: String) {
    KIKI("kiki"),
    BOBO("bobo"),
    NANA("nana"),
    CHACHA("chacha"),
    BOOBOO("booboo");

    companion object {
        fun fromKey(key: String): Character? {
            return entries.find { it.key == key }
        }
    }
}
