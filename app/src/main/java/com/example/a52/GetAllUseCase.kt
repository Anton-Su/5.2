package com.example.a52.ui.theme

class GetAllUseCase(private val repository: Repository) {
    suspend operator fun invoke(): List<RepositoryItem> {
        return repository.getAll()
    }
}