package com.ggaebiz.ggaebiz.domain.repository

interface OnboardingRepository {
    suspend fun setIsOnboardingExposed(value : Boolean)
    suspend fun getIsOnboardingExposed() : Boolean
}
