package com.appFiles.injaewook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appFiles.injaewook.ui.theme.InJaeWookTheme
import com.appFiles.injaewook.viewModelFiles.EmotionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestScreen()
        }
    }

}

//테스트화면
@Composable
fun TestScreen(viewModel: EmotionViewModel =viewModel()) {
    // 뷰모델의 데이터를 화면에서 감지(Observe)
    val results by viewModel.emotionResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 입력창 텍스트 상태
    var textInput by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 텍스트 입력창
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("분석할 문장을 입력하세요") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        // 분석 버튼
        Button(
            onClick = { viewModel.analyzeText(textInput) },
            enabled = !isLoading && textInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("AI 감정 분석 시작")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 44개 감정 결과 리스트 출력
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(results) { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = data.emotion, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${data.probability}%", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}