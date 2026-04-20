package com.example.a52

import java.io.File

interface Repository {
    suspend fun getAll(): List<RepositoryItem>
    fun createImageFile(): File?
}