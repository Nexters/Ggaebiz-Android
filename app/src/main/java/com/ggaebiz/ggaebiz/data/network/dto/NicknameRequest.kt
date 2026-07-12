package com.ggaebiz.ggaebiz.data.network.dto

import com.google.gson.annotations.SerializedName

data class NicknameRequest(
    @SerializedName("nickname")
    val nickname: String,
)
