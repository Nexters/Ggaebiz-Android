package com.ggaebiz.ggaebiz.presentation.ui.setting

sealed interface TimerMode {
    val maxHour: Int
    val maxMinute: Int
    val defaultHour: String
    val defaultMinute: String

    fun isRestTimer(): Boolean = this is Rest
    fun isConcentrateTimer(): Boolean = this is Concentrate

    fun toRecordFields(): Pair<String, String?> = when (this) {
        is Rest -> "REST" to null
        is Concentrate -> "CONCENTRATE" to type.name
    }

    data class Rest(val type: RestType) : TimerMode {

        override val maxHour: Int
            get() = when (type) {
                RestType.NORMAL -> 5
            }

        override val maxMinute: Int
            get() = when (type) {
                RestType.NORMAL -> 59
            }

        override val defaultHour: String
            get() = when (type) {
                RestType.NORMAL -> "00"
            }

        override val defaultMinute: String
            get() = when (type) {
                RestType.NORMAL -> "15"
            }
    }

    data class Concentrate(val type: ConcentrateType) : TimerMode {

        fun isNormal(): Boolean = this.type == ConcentrateType.NORMAL
        fun isStudy(): Boolean = this.type == ConcentrateType.STUDY
        fun isExercise(): Boolean = this.type == ConcentrateType.EXERCISE

        override val maxHour: Int
            get() = when (type) {
                ConcentrateType.NORMAL -> 5
                ConcentrateType.STUDY -> 5
                ConcentrateType.EXERCISE -> 2
            }

        override val maxMinute: Int
            get() = when (type) {
                ConcentrateType.NORMAL -> 59
                ConcentrateType.STUDY -> 59
                ConcentrateType.EXERCISE -> 59
            }

        override val defaultHour: String
            get() = when (type) {
                ConcentrateType.NORMAL -> "00"
                ConcentrateType.STUDY -> "02"
                ConcentrateType.EXERCISE -> "01"
            }

        override val defaultMinute: String
            get() = when (type) {
                ConcentrateType.NORMAL -> "30"
                ConcentrateType.STUDY -> "00"
                ConcentrateType.EXERCISE -> "00"
            }
    }
}

enum class ConcentrateType { NORMAL, STUDY, EXERCISE }
enum class RestType { NORMAL }

fun toTimerMode(timerMode: String, concentrateType: String?): TimerMode =
    when (timerMode) {
        "REST" -> {
            val type = when (concentrateType) {
                "NORMAL" -> RestType.NORMAL
                else -> RestType.NORMAL
            }
            TimerMode.Rest(type)
        }

        "CONCENTRATE" -> {
            val type = when (concentrateType) {
                "STUDY" -> ConcentrateType.STUDY
                "EXERCISE" -> ConcentrateType.EXERCISE
                else -> ConcentrateType.NORMAL
            }
            TimerMode.Concentrate(type)
        }

        else -> {
            TimerMode.Rest(RestType.NORMAL)
        }
    }

fun toTimerModeString(timerMode: TimerMode): Pair<String, String> =
    when (timerMode) {
        is TimerMode.Rest -> {
            val type = when (timerMode.type) {
                RestType.NORMAL -> "NORMAL"
            }
            "REST" to type
        }

        is TimerMode.Concentrate -> {
            val type = when (timerMode.type) {
                ConcentrateType.STUDY -> "STUDY"
                ConcentrateType.EXERCISE -> "EXERCISE"
                ConcentrateType.NORMAL -> "NORMAL"
            }
            "CONCENTRATE" to type
        }
    }
