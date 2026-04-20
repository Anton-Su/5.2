package com.example.a52.ui.theme

class DeleteItemUseCase(private val repository: Repository) {
    suspend operator fun invoke(item: RepositoryItem): List<RepositoryItem> {
        return repository.deleteItem(item)
    }
}