package com.fedorskvortsov.pics.ui.state

sealed class ImageListUiState {
    data class Success(val images: List<String>) : ImageListUiState()
    data class Error(val exception: Throwable) : ImageListUiState()
    object Loading : ImageListUiState()
}
