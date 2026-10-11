package com.ggaebiz.ggaebiz.data.network

import com.ggaebiz.ggaebiz.data.datastore.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authDataStore: AuthDataStore,
) : Interceptor {
    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.header(HEADER_AUTHORIZATION) != null) {
            return chain.proceed(request)
        }

        val token = runBlocking { authDataStore.getAccessToken().firstOrNull() }
        val newRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader(HEADER_AUTHORIZATION, "$BEARER_PREFIX$token")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}
