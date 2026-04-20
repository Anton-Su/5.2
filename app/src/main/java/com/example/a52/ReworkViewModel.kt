package com.example.a52

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File


class ReworkViewModel(private val getAll: GetAllUseCase,
                      private val createImageFileUseCase: CreateImageFileUseCase
): ViewModel() {
    private val _repos = MutableStateFlow<List<RepositoryItem>>(emptyList())
    val repos = _repos.asStateFlow()

    init {
        loadAll()
    }
    private fun loadAll() {
        viewModelScope.launch {
            _repos.value = getAll()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _repos.value = getAll()
        }
    }

    fun createImageFile(): File? {
        return try {
            createImageFileUseCase()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
