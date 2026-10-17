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

    fun sendPrompt(bitmap: Bitmap?, prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val generativeModel = Firebase.ai.generativeModel(
                    modelName = "gemini-flash-latest",
                )

                val contentPayload = content {
                    bitmap?.let {
                        image(it.scaleDown(768))
                    }
                    text(prompt)
                }

                var outputText = ""
                generativeModel.generateContentStream(contentPayload).collect { chunk ->
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

    private fun Bitmap.scaleDown(maxDimension: Int = 768): Bitmap {
        val ratio = Math.min(
            maxDimension.toFloat() / width,
            maxDimension.toFloat() / height
        )
        if (ratio >= 1.0f) return this
        val newWidth = Math.round(ratio * width)
        val newHeight = Math.round(ratio * height)
        return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    }
}