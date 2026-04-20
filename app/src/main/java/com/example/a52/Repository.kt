package com.example.a52.ui.theme

interface Repository {
    suspend fun getAll(): List<RepositoryItem>
    suspend fun addItem(title: String, text: String): List<RepositoryItem>
    suspend fun deleteItem(item: RepositoryItem): List<RepositoryItem>
}