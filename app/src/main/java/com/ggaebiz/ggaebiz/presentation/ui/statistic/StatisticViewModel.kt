package com.ggaebiz.ggaebiz.presentation.ui.statistic

import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class StatisticState(
    val placeholder: Unit = Unit
)

sealed interface StatisticSideEffect {
    data object NavigateBack : StatisticSideEffect
}

sealed interface StatisticIntent {
    data object ClickBack : StatisticIntent
}

class StatisticViewModel : BaseViewModel<StatisticState, StatisticIntent, StatisticSideEffect>(StatisticState()) {

    fun processIntent(intent: StatisticIntent) {
        when (intent) {
            StatisticIntent.ClickBack -> postSideEffect(StatisticSideEffect.NavigateBack)
        }
    }
}