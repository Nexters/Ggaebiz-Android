package com.ggaebiz.ggaebiz.data.repository

import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore.Companion.DEFAULT_IS_ONBOARDING_EXPOSED
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull

class OnboardingRepositoryImpl(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingRepository {
    override suspend fun setIsOnboardingExposed(value: Boolean) =
        onboardingDataStore.setIsOnboardingExposed(value)

    override suspend fun getIsOnboardingExposed(): Boolean =
        onboardingDataStore.getIsOnboardingExposed().firstOrNull() ?: DEFAULT_IS_ONBOARDING_EXPOSED
}
