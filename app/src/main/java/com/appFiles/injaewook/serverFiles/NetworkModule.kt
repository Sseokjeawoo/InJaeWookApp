package com.appFiles.injaewook.serverFiles

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 1. 서버와 주고받을 데이터 모양 정의
data class EmotionRequest(val text: String)

data class EmotionResponse(
    val status: String,
    val data: List<EmotionData>
)

data class EmotionData(
    val emotion: String,
    val probability: Double
)

// 2. API 엔드포인트(URL) 정의
interface EmotionApi {
    @POST("/analyze")
    suspend fun analyzeText(@Body request: EmotionRequest): EmotionResponse
}

// 3. 통신 객체 생성 (안드로이드 에뮬레이터 전용 로컬호스트 IP 적용)
object RetrofitClient {
    private const val BASE_URL = "http://172.30.1.97:8000"

    val api: EmotionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmotionApi::class.java)
    }
}