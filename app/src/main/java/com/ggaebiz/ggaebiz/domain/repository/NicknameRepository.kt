package com.ggaebiz.ggaebiz.domain.repository

interface NicknameRepository {
    /** 랜덤 형용사 + 캐릭터 이름으로 닉네임을 생성한다. */
    fun generateRandomNickname(): String

    /** 로컬에 저장된 현재 닉네임을 반환한다. */
    suspend fun getNickname(): String?

    /** 서버에 닉네임을 반영하고, 성공 시 로컬에도 저장한다. */
    suspend fun updateNickname(nickname: String): Result<Unit>
}
