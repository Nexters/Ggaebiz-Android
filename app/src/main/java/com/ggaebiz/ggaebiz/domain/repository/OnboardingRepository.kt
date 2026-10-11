package com.ggaebiz.ggaebiz.domain.repository

interface OnboardingRepository {
    suspend fun setIsOnboardingExposed(value : Boolean)
    suspend fun getIsOnboardingExposed() : Boolean

    suspend fun setHomeNudgeGuideViewed(value: Boolean)
    suspend fun getHomeNudgeGuideViewed(): Boolean

    suspend fun setSettingNudgeGuideViewed(value: Boolean)
    suspend fun getSettingNudgeGuideViewed(): Boolean

    suspend fun setBatteryPopupViewed(value: Boolean)
    suspend fun getBatteryPopupViewed(): Boolean

    suspend fun clearOnboarding()
}
