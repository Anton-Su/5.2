package com.example.a52.ui.theme

class AddItemUseCase(private val repository: Repository) {
    suspend operator fun invoke(title: String, text: String): List<RepositoryItem> {
        return repository.addItem(title, text)
    }
}