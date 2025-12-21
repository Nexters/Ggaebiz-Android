package com.ggaebiz.ggaebiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AlarmCharacterData(
    val characterName: CharacterName,
    val restMentAudioList: PersistentList<PersistentList<MentAudio>>,
    val concentrateNormalMentAudioList: PersistentList<MentAudio>,
    val concentrateStudyMentAudioList: PersistentList<MentAudio>,
    val concentrateExerciseMentAudioList: PersistentList<MentAudio>,
    @DrawableRes val alarmBackgroundImageList: PersistentList<Int>,
) {
    fun getMentAudio(timerMode: TimerMode, level: Int? = null, levelIdx: Int? = null): MentAudio {
        return when {
            timerMode.isRestTimer() -> {
                if (level != null && levelIdx != null) {
                    restMentAudioList[level][levelIdx]
                } else {
                    restMentAudioList[0][0]
                }
            }
            timerMode.isConcentrateTimer() -> {
                when {
                    (timerMode as TimerMode.Concentrate).isNormal() -> {
                        concentrateNormalMentAudioList[0]
                    }
                    (timerMode as TimerMode.Concentrate).isStudy() -> {
                        concentrateStudyMentAudioList[0]
                    }
                    (timerMode as TimerMode.Concentrate).isExercise() -> {
                        concentrateExerciseMentAudioList[0]
                    }

                    else -> concentrateNormalMentAudioList[0]
                }
            }

            else -> restMentAudioList[0][0]
        }
    }

    companion object {


        private val ALARM_CHARACTER_DATA = listOf(
            AlarmCharacterData(
                characterName = CharacterName.KIKI,
                restMentAudioList = persistentListOf(
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
                concentrateNormalMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.focus_ment_kiki_1,
                        audioPath = "raw/kiki_focus_1"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_kiki_2,
                        audioPath = "raw/kiki_focus_2"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_kiki_3,
                        audioPath = "raw/kiki_focus_3"
                    ),
                ),
                concentrateStudyMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.study_ment_kiki_1,
                        audioPath = "raw/kiki_study_1"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_kiki_2,
                        audioPath = "raw/kiki_study_2"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_kiki_3,
                        audioPath = "raw/kiki_study_3"
                    ),
                ),
                concentrateExerciseMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.exercise_ment_kiki_1,
                        audioPath = "raw/kiki_exercise_1"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_kiki_2,
                        audioPath = "raw/kiki_exercise_2"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_kiki_3,
                        audioPath = "raw/kiki_exercise_3"
                    ),
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
                restMentAudioList = persistentListOf(
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
                concentrateNormalMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.focus_ment_bobo_1,
                        audioPath = "raw/bobo_focus_1"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_bobo_2,
                        audioPath = "raw/bobo_focus_2"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_bobo_3,
                        audioPath = "raw/bobo_focus_3"
                    ),
                ),
                concentrateStudyMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.study_ment_bobo_1,
                        audioPath = "raw/bobo_study_1"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_bobo_2,
                        audioPath = "raw/bobo_study_2"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_bobo_3,
                        audioPath = "raw/bobo_study_3"
                    ),
                ),
                concentrateExerciseMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.exercise_ment_bobo_1,
                        audioPath = "raw/bobo_exercise_1"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_bobo_2,
                        audioPath = "raw/bobo_exercise_2"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_bobo_3,
                        audioPath = "raw/bobo_exercise_3"
                    ),
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
                restMentAudioList = persistentListOf(
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
                concentrateNormalMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.focus_ment_nana_1,
                        audioPath = "raw/nana_focus_1"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_nana_2,
                        audioPath = "raw/nana_focus_2"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_nana_3,
                        audioPath = "raw/nana_focus_3"
                    ),
                ),
                concentrateStudyMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.study_ment_nana_1,
                        audioPath = "raw/nana_study_1"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_nana_2,
                        audioPath = "raw/nana_study_2"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_nana_3,
                        audioPath = "raw/nana_study_3"
                    ),
                ),
                concentrateExerciseMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.exercise_ment_nana_1,
                        audioPath = "raw/nana_exercise_1"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_nana_2,
                        audioPath = "raw/nana_exercise_2"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_nana_3,
                        audioPath = "raw/nana_exercise_3"
                    ),
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
                restMentAudioList = persistentListOf(
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
                concentrateNormalMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.focus_ment_chacha_1,
                        audioPath = "raw/chacha_focus_1"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_chacha_2,
                        audioPath = "raw/chacha_focus_2"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_chacha_3,
                        audioPath = "raw/chacha_focus_3"
                    ),
                ),
                concentrateStudyMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.study_ment_chacha_1,
                        audioPath = "raw/chacha_study_1"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_chacha_2,
                        audioPath = "raw/chacha_study_2"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_chacha_3,
                        audioPath = "raw/chacha_study_3"
                    ),
                ),
                concentrateExerciseMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.exercise_ment_chacha_1,
                        audioPath = "raw/chacha_exercise_1"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_chacha_2,
                        audioPath = "raw/chacha_exercise_2"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_chacha_3,
                        audioPath = "raw/chacha_exercise_3"
                    ),
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
                restMentAudioList = persistentListOf(
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
                concentrateNormalMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.focus_ment_booboo_1,
                        audioPath = "raw/booboo_focus_1"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_booboo_2,
                        audioPath = "raw/booboo_focus_2"
                    ),
                    MentAudio(
                        ment = R.string.focus_ment_booboo_3,
                        audioPath = "raw/booboo_focus_3"
                    ),
                ),
                concentrateStudyMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.study_ment_booboo_1,
                        audioPath = "raw/booboo_study_1"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_booboo_2,
                        audioPath = "raw/booboo_study_2"
                    ),
                    MentAudio(
                        ment = R.string.study_ment_booboo_3,
                        audioPath = "raw/booboo_study_3"
                    ),
                ),
                concentrateExerciseMentAudioList = persistentListOf(
                    MentAudio(
                        ment = R.string.exercise_ment_booboo_1,
                        audioPath = "raw/booboo_exercise_1"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_booboo_2,
                        audioPath = "raw/booboo_exercise_2"
                    ),
                    MentAudio(
                        ment = R.string.exercise_ment_booboo_3,
                        audioPath = "raw/booboo_exercise_3"
                    ),
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
