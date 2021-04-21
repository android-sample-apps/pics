package com.fedorskvortsov.pics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedorskvortsov.pics.domain.repository.ImageRepository
import com.fedorskvortsov.pics.ui.state.ImageListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageListUiState>(ImageListUiState.Loading)
    val uiState: StateFlow<ImageListUiState> = _uiState

    init {
        getImages()
    }

    fun getImages() {
        _uiState.value = ImageListUiState.Loading
        viewModelScope.launch {
            repository.images
                .catch { exception ->
                    _uiState.value = ImageListUiState.Error(exception)
                }
                .collect { images ->
                    _uiState.value = ImageListUiState.Success(images)
                }
        }
    }
}
