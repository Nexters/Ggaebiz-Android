package com.ggaebiz.ggaebiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Character(
    val key: CharacterName,
    @StringRes val nameResId: Int,
    @StringRes val wholeNameResId: Int,
    @StringRes val initMentResId: Int,
    @StringRes val timerMentResId: Int,
    @DrawableRes val imageResId: PersistentList<Int>,
    @DrawableRes val selectedImageResId: PersistentList<Int>,
    @StringRes val traitsResIdList: PersistentList<Int>,
    @DrawableRes val alarmBackgroundImageList: PersistentList<Int>,
    val alarmMentList: List<List<Int>>,
) {
    companion object {
        val CHARACTER_LIST = listOf(
            Character(
                key = CharacterName.KIKI,
                nameResId = R.string.kiki_name,
                wholeNameResId = R.string.kiki_name_text,
                initMentResId = R.string.kiki_init_ment_text,
                timerMentResId = R.string.kiki_timer_ment_text,
                imageResId = persistentListOf(
                    R.drawable.ic_kiki_level1,
                    R.drawable.ic_kiki_level2,
                    R.drawable.ic_kiki_level3,
                ),
                selectedImageResId = persistentListOf(
                    R.drawable.ic_selected_kiki_level1,
                    R.drawable.ic_selected_kiki_level2,
                    R.drawable.ic_selected_kiki_level3,
                ),
                traitsResIdList = persistentListOf(
                    R.string.kiki_tag_text1,
                    R.string.kiki_tag_text2,
                    R.string.kiki_tag_text3,
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
                alarmMentList = listOf(
                    listOf(
                        R.string.alarm_ment_kiki_level1_1,
                        R.string.alarm_ment_kiki_level1_2,
                        R.string.alarm_ment_kiki_level1_3
                    ),
                    listOf(
                        R.string.alarm_ment_kiki_level2_1,
                        R.string.alarm_ment_kiki_level2_2,
                        R.string.alarm_ment_kiki_level2_3
                    ),
                    listOf(
                        R.string.alarm_ment_kiki_level3_1,
                        R.string.alarm_ment_kiki_level3_2,
                        R.string.alarm_ment_kiki_level3_3,
                        R.string.alarm_ment_kiki_level3_4
                    ),
                    listOf(
                        R.string.alarm_ment_kiki_level4_1,
                    )
                )
            ),
            Character(
                nameResId = R.string.bobo_name,
                key = CharacterName.BOBO,
                wholeNameResId = R.string.bobo_name_text,
                initMentResId = R.string.bobo_init_ment_text,
                timerMentResId = R.string.bobo_timer_ment_text,
                imageResId = persistentListOf(
                    R.drawable.ic_bobo_level1,
                    R.drawable.ic_bobo_level2,
                    R.drawable.ic_bobo_level3,
                ),
                selectedImageResId = persistentListOf(
                    R.drawable.ic_selected_bobo_level1,
                    R.drawable.ic_selected_bobo_level2,
                    R.drawable.ic_selected_bobo_level3,
                ),
                traitsResIdList = persistentListOf(
                    R.string.bobo_tag_text1,
                    R.string.bobo_tag_text2,
                    R.string.bobo_tag_text3,
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
                alarmMentList = listOf(
                    listOf(
                        R.string.alarm_ment_bobo_level1_1,
                        R.string.alarm_ment_bobo_level1_2,
                        R.string.alarm_ment_bobo_level1_3
                    ),
                    listOf(
                        R.string.alarm_ment_bobo_level2_1,
                        R.string.alarm_ment_bobo_level2_2,
                        R.string.alarm_ment_bobo_level2_3
                    ),
                    listOf(
                        R.string.alarm_ment_bobo_level3_1,
                        R.string.alarm_ment_bobo_level3_2,
                        R.string.alarm_ment_bobo_level3_3,
                        R.string.alarm_ment_bobo_level3_4
                    ),
                    listOf(
                        R.string.alarm_ment_bobo_level4_1,
                    )
                )
            ),
            Character(
                nameResId = R.string.nana_name,
                key = CharacterName.NANA,
                wholeNameResId = R.string.nana_name_text,
                initMentResId = R.string.nana_init_ment_text,
                timerMentResId = R.string.nana_timer_ment_text,
                imageResId = persistentListOf(
                    R.drawable.ic_nana_level1,
                    R.drawable.ic_nana_level2,
                    R.drawable.ic_nana_level3,
                ),
                selectedImageResId = persistentListOf(
                    R.drawable.ic_selected_nana_level1,
                    R.drawable.ic_selected_nana_level2,
                    R.drawable.ic_selected_nana_level3,
                ),
                traitsResIdList = persistentListOf(
                    R.string.nana_tag_text1,
                    R.string.nana_tag_text2,
                    R.string.nana_tag_text3,
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
                alarmMentList = listOf(
                    listOf(
                        R.string.alarm_ment_nana_level1_1,
                        R.string.alarm_ment_nana_level1_2,
                        R.string.alarm_ment_nana_level1_3
                    ),
                    listOf(
                        R.string.alarm_ment_nana_level2_1,
                        R.string.alarm_ment_nana_level2_2,
                        R.string.alarm_ment_nana_level2_3
                    ),
                    listOf(
                        R.string.alarm_ment_nana_level3_1,
                        R.string.alarm_ment_nana_level3_2,
                        R.string.alarm_ment_nana_level3_3,
                        R.string.alarm_ment_nana_level3_4
                    ),
                    listOf(
                        R.string.alarm_ment_nana_level4_1,
                    )
                )
            ),
            Character(
                nameResId = R.string.chacha_name,
                key = CharacterName.CHACHA,
                wholeNameResId = R.string.chacha_name_text,
                initMentResId = R.string.chacha_init_ment_text,
                timerMentResId = R.string.chacha_timer_ment_text,
                imageResId = persistentListOf(
                    R.drawable.ic_chacha_level1,
                    R.drawable.ic_chacha_level2,
                    R.drawable.ic_chacha_level3,
                ),
                selectedImageResId = persistentListOf(
                    R.drawable.ic_selected_chacha_level1,
                    R.drawable.ic_selected_chacha_level2,
                    R.drawable.ic_selected_chacha_level3,
                ),
                traitsResIdList = persistentListOf(
                    R.string.chacha_tag_text1,
                    R.string.chacha_tag_text2,
                    R.string.chacha_tag_text3,
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
                alarmMentList = listOf(
                    listOf(
                        R.string.alarm_ment_chacha_level1_1,
                        R.string.alarm_ment_chacha_level1_2,
                        R.string.alarm_ment_chacha_level1_3
                    ),
                    listOf(
                        R.string.alarm_ment_chacha_level2_1,
                        R.string.alarm_ment_chacha_level2_2,
                        R.string.alarm_ment_chacha_level2_3
                    ),
                    listOf(
                        R.string.alarm_ment_chacha_level3_1,
                        R.string.alarm_ment_chacha_level3_2,
                        R.string.alarm_ment_chacha_level3_3,
                        R.string.alarm_ment_chacha_level3_4
                    ),
                    listOf(
                        R.string.alarm_ment_chacha_level4_1,
                    )
                )
            ),
            Character(
                nameResId = R.string.booboo_name,
                key = CharacterName.BOOBOO,
                wholeNameResId = R.string.booboo_name_text,
                initMentResId = R.string.booboo_init_ment_text,
                timerMentResId = R.string.booboo_timer_ment_text,
                imageResId = persistentListOf(
                    R.drawable.ic_booboo_level1,
                    R.drawable.ic_booboo_level2,
                    R.drawable.ic_booboo_level3,
                ),
                selectedImageResId = persistentListOf(
                    R.drawable.ic_selected_booboo_level1,
                    R.drawable.ic_selected_booboo_level2,
                    R.drawable.ic_selected_booboo_level3,
                ),
                traitsResIdList = persistentListOf(
                    R.string.booboo_tag_text1,
                    R.string.booboo_tag_text2,
                    R.string.booboo_tag_text3,
                ),
                alarmBackgroundImageList = persistentListOf(
                    R.drawable.fullpage_kiki_lev_1,
                    R.drawable.fullpage_kiki_lev_24,
                    R.drawable.fullpage_kiki_lev_3,
                    R.drawable.fullpage_kiki_lev_24
                ),
                alarmMentList = listOf(
                    listOf(
                        R.string.alarm_ment_booboo_level1_1,
                        R.string.alarm_ment_booboo_level1_2,
                        R.string.alarm_ment_booboo_level1_3
                    ),
                    listOf(
                        R.string.alarm_ment_booboo_level2_1,
                        R.string.alarm_ment_booboo_level2_2,
                        R.string.alarm_ment_booboo_level2_3
                    ),
                    listOf(
                        R.string.alarm_ment_booboo_level3_1,
                        R.string.alarm_ment_booboo_level3_2,
                        R.string.alarm_ment_booboo_level3_3,
                        R.string.alarm_ment_booboo_level3_4
                    ),
                    listOf(
                        R.string.alarm_ment_booboo_level4_1,
                    )
                )
            ),
        )

        val SETTING_MENT_LIST = listOf(
            R.string.setting_ment_text1,
            R.string.setting_ment_text2,
            R.string.setting_ment_text3,
        )
    }
}
