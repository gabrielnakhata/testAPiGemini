package com.agendaon.testapigemini

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BakingViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    fun sendPrompt(bitmap: Bitmap, prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val generativeModel = Firebase.ai.generativeModel(
                    modelName = "gemini-flash-latest",
                )

                var outputText = ""
                // Usamos generateContentStream em vez de generateContent, Com o streaming,
                // à medida que o Gemini vai gerando cada palavra, ele já envia..
                generativeModel.generateContentStream(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                ).collect { chunk ->
                    outputText += chunk.text ?: ""
                    _uiState.value = UiState.Success(outputText)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.localizedMessage ?: "Erro ao gerar resposta."
                )
            }
        }
    }
}