package com.example.a52.ui.theme

import kotlinx.coroutines.delay

class RepositoryImpl(private val dataSource: DataTxtSource): Repository {
    private var repos: MutableList<RepositoryItem>? = null
    private suspend fun ensureLoaded() {
        if (repos == null) {
            val list = dataSource.getRepos()
            repos = list.toMutableList()
        }
    }

    override suspend fun getAll(): List<RepositoryItem> {
        ensureLoaded()
        delay(1000)
        return repos ?: emptyList()
    }

    override suspend fun addItem(title: String, text: String): List<RepositoryItem> {
        ensureLoaded()
        val newRepositoryItem = dataSource.saveItem(title, text)
        val updated = repos ?: mutableListOf()
        updated.add(0, newRepositoryItem) // add to front so sorted desc
        repos = updated
        return repos ?: emptyList()
    }

    override suspend fun deleteItem(item: RepositoryItem): List<RepositoryItem> {
        ensureLoaded()
        repos = repos?.filter { it != item }?.toMutableList()
        return repos ?: emptyList()
    }
}