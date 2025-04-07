package com.ggaebiz.ggaebiz.data.repository

import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore.Companion.DEFAULT_IS_BATTERY_POPUP_VIEWED
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore.Companion.DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore.Companion.DEFAULT_IS_ONBOARDING_EXPOSED
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore.Companion.DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull

class OnboardingRepositoryImpl(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingRepository {
    override suspend fun setIsOnboardingExposed(value: Boolean) =
        onboardingDataStore.setIsOnboardingExposed(value)

    override suspend fun getIsOnboardingExposed(): Boolean =
        onboardingDataStore.getIsOnboardingExposed().firstOrNull() ?: DEFAULT_IS_ONBOARDING_EXPOSED

    override suspend fun setHomeNudgeGuideViewed(value: Boolean) =
        onboardingDataStore.setHomeNudgeGuideViewed(value)

    override suspend fun getHomeNudgeGuideViewed(): Boolean =
        onboardingDataStore.getHomeNudgeGuideViewed().firstOrNull() ?: DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED

    override suspend fun setSettingNudgeGuideViewed(value: Boolean) =
        onboardingDataStore.setSettingNudgeGuideViewed(value)

    override suspend fun getSettingNudgeGuideViewed(): Boolean =
        onboardingDataStore.getSettingNudgeGuideViewed().firstOrNull() ?: DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED

    override suspend fun setBatteryPopupViewed(value: Boolean) =
        onboardingDataStore.setBatteryPopupViewed(value)

    override suspend fun getBatteryPopupViewed(): Boolean =
        onboardingDataStore.getBatteryPopupViewed().firstOrNull() ?: DEFAULT_IS_BATTERY_POPUP_VIEWED
}
