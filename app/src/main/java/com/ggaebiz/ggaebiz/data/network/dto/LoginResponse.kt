package com.ggaebiz.ggaebiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("userId")
    val userId: Int,
)
