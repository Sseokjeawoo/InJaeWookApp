package com.appFiles.injaewook.viewModelFiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appFiles.injaewook.serverFiles.EmotionData
import com.appFiles.injaewook.serverFiles.EmotionRequest
import com.appFiles.injaewook.serverFiles.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmotionViewModel : ViewModel() {

    // 화면에 보여줄 분석 결과 리스트 (상태 보관)
    private val _emotionResults = MutableStateFlow<List<EmotionData>>(emptyList())
    val emotionResults: StateFlow<List<EmotionData>> = _emotionResults

    // 로딩 상태 (버튼 연타 방지 및 UI 표시용)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 1. 에러 메시지 보관 변수 추가
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    // 서버에 텍스트를 보내고 결과를 받아오는 함수
    fun analyzeText(inputText: String) {
        if (inputText.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Retrofit을 통해 서버에 POST 요청
                val response = RetrofitClient.api.analyzeText(EmotionRequest(text = inputText))

                // 성공 시 결과를 상태에 저장하여 UI 갱신
                if (response.status == "success") {
                    _emotionResults.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 발생 시 처리 로직 (실무에서는 에러 메시지를 띄움)
                _errorMessage.value = "에러 발생: ${e.message}" // 화면에 띄울 에러 텍스트 저장
            } finally {
                _isLoading.value = false
            }
        }
    }
}