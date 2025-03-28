package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ggaebiz.ggaebiz.R

data class Onboarding (
    @DrawableRes val onBoardingImgRes: Int,
    @StringRes val descriptionRes: Int,
) {
    companion object {
        val ONBOARDING_LIST = listOf(
            Onboarding(
                onBoardingImgRes = R.drawable.img_onboarding_1,
                descriptionRes = R.string.onboarding_text1,
            ),
            Onboarding(
                onBoardingImgRes = R.drawable.img_onboarding_2,
                descriptionRes = R.string.onboarding_text2,
            ),
            Onboarding(
                onBoardingImgRes = R.drawable.img_onboarding_3,
                descriptionRes = R.string.onboarding_text3,
            ),
        )
    }
}
