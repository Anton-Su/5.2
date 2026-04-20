package com.example.a52

import kotlinx.coroutines.delay
import java.io.File

class RepositoryImpl(private val dataSource: DataTxtSource): Repository {
    private var repos: MutableList<RepositoryItem>? = null


    override suspend fun getAll(): List<RepositoryItem> {
        val list = dataSource.getRepos()
        repos = list.toMutableList()
        delay(100)
        return repos ?: emptyList()
    }


    override fun createImageFile(): File? {
        return dataSource.createImageFile()
    }
}