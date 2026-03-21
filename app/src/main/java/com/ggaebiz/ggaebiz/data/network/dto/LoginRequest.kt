package com.ggaebiz.ggaebiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("provider")
    val provider: String = "KAKAO",
    @SerializedName("token")
    val token: String,
)
