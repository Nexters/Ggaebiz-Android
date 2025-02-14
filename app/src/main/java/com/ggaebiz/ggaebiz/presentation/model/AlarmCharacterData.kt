package com.ggaebiz.ggaebiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable


@Serializable
data class AlarmCharacterData(
    val characterName: CharacterName,
    val mentAudioList: PersistentList<PersistentList<MentAudio>>,
    @DrawableRes val alarmBackgroundImageList: PersistentList<Int>,
) {
    companion object {
        private val ALARM_CHARACTER_DATA = listOf(
            AlarmCharacterData(
                characterName = CharacterName.KIKI,
                mentAudioList = persistentListOf(
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level1_1,
                            audioPath = "raw/kiki_level1_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level1_2,
                            audioPath = "raw/kiki_level1_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level1_3,
                            audioPath = "raw/kiki_level1_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level2_1,
                            audioPath = "raw/kiki_level2_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level2_2,
                            audioPath = "raw/kiki_level2_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level2_3,
                            audioPath = "raw/kiki_level2_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level3_1,
                            audioPath = "raw/kiki_level3_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level3_2,
                            audioPath = "raw/kiki_level3_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level3_3,
                            audioPath = "raw/kiki_level3_3"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level3_4,
                            audioPath = "raw/kiki_level3_4"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_kiki_level4_1,
                            audioPath = "raw/kiki_last"
                        ),
                    )
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
            ),
            AlarmCharacterData(
                characterName = CharacterName.BOBO,
                mentAudioList = persistentListOf(
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level1_1,
                            audioPath = "raw/bobo_level1_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level1_2,
                            audioPath = "raw/bobo_level1_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level1_3,
                            audioPath = "raw/bobo_level1_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level2_1,
                            audioPath = "raw/bobo_level2_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level2_2,
                            audioPath = "raw/bobo_level2_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level2_3,
                            audioPath = "raw/bobo_level2_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level3_1,
                            audioPath = "raw/bobo_level3_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level3_2,
                            audioPath = "raw/bobo_level3_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level3_3,
                            audioPath = "raw/bobo_level3_3"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level3_4,
                            audioPath = "raw/bobo_level3_4"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_bobo_level4_1,
                            audioPath = "raw/bobo_last"
                        ),
                    )
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_bobo_lev_1,
                    R.drawable.fullpage_bobo_lev_24,
                    R.drawable.fullpage_bobo_lev_3,
                    R.drawable.fullpage_bobo_lev_24
                ),
            ),
            AlarmCharacterData(
                characterName = CharacterName.NANA,
                mentAudioList = persistentListOf(
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level1_1,
                            audioPath = "raw/nana_level1_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level1_2,
                            audioPath = "raw/nana_level1_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level1_3,
                            audioPath = "raw/nana_level1_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level2_1,
                            audioPath = "raw/nana_level2_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level2_2,
                            audioPath = "raw/nana_level2_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level2_3,
                            audioPath = "raw/nana_level2_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level3_1,
                            audioPath = "raw/nana_level3_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level3_2,
                            audioPath = "raw/nana_level3_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level3_3,
                            audioPath = "raw/nana_level3_3"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level3_4,
                            audioPath = "raw/nana_level3_4"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_nana_level4_1,
                            audioPath = "raw/nana_last"
                        ),
                    )
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_nana_lev_1,
                    R.drawable.fullpage_nana_lev_24,
                    R.drawable.fullpage_nana_lev_3,
                    R.drawable.fullpage_nana_lev_24
                ),
            ),
            AlarmCharacterData(
                characterName = CharacterName.CHACHA,
                mentAudioList = persistentListOf(
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level1_1,
                            audioPath = "raw/chacha_level1_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level1_2,
                            audioPath = "raw/chacha_level1_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level1_3,
                            audioPath = "raw/chacha_level1_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level2_1,
                            audioPath = "raw/chacha_level2_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level2_2,
                            audioPath = "raw/chacha_level2_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level2_3,
                            audioPath = "raw/chacha_level2_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level3_1,
                            audioPath = "raw/chacha_level3_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level3_2,
                            audioPath = "raw/chacha_level3_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level3_3,
                            audioPath = "raw/chacha_level3_3"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level3_4,
                            audioPath = "raw/chacha_level3_4"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_chacha_level4_1,
                            audioPath = "raw/chacha_last"
                        ),
                    )
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_chacha_lev_1,
                    R.drawable.fullpage_chacha_lev_24,
                    R.drawable.fullpage_chacha_lev_3,
                    R.drawable.fullpage_chacha_lev_24
                ),
            ),
            AlarmCharacterData(
                characterName = CharacterName.BOOBOO,
                mentAudioList = persistentListOf(
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level1_1,
                            audioPath = "raw/booboo_1_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level1_2,
                            audioPath = "raw/booboo_1_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level1_3,
                            audioPath = "raw/booboo_1_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level2_1,
                            audioPath = "raw/booboo_2_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level2_2,
                            audioPath = "raw/booboo_2_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level2_3,
                            audioPath = "raw/booboo_2_3"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level3_1,
                            audioPath = "raw/booboo_3_1"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level3_2,
                            audioPath = "raw/booboo_3_2"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level3_3,
                            audioPath = "raw/booboo_3_3"
                        ),
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level3_4,
                            audioPath = "raw/booboo_3_4"
                        )
                    ),
                    persistentListOf(
                        MentAudio(
                            ment = R.string.alarm_ment_booboo_level4_1,
                            audioPath = "raw/booboo_last"
                        ),
                    )
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_booboo_lev_1,
                    R.drawable.fullpage_booboo_lev_24,
                    R.drawable.fullpage_booboo_lev_3,
                    R.drawable.fullpage_booboo_lev_24
                )
            )
        )
        val ALARM_CHARACTER_MAP: Map<CharacterName, AlarmCharacterData> =
            ALARM_CHARACTER_DATA.associateBy { it.characterName }
    }
}

@Serializable
data class MentAudio(
    @StringRes val ment: Int,
    val audioPath: String,
)