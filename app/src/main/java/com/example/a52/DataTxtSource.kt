package com.example.a52.ui.theme

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DataTxtSource(private val context: Context) {
    suspend fun getRepos(): List<RepositoryItem> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files
                ?.filter { it.extension == "txt" }
                ?.map { file ->
                    val text = file.readText()
                    val nameNoExt = file.nameWithoutExtension
                    val timestamp = nameNoExt.substringBefore("_").toLongOrNull() ?: 0L
                    val fileName = nameNoExt.substringAfter("_")
                    RepositoryItem(
                        fileName = fileName,
                        text = text,
                        timestamp = timestamp
                    )
                }
                ?.sortedByDescending { it.timestamp }
                ?: emptyList()
        }
    }

    suspend fun saveItem(title: String, text: String): RepositoryItem {
        return withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            val safeTitle = if (title.isBlank()) "untitled" else title.replace(" ", "_")
            val fileName = "${timestamp}_${safeTitle}.txt"
            val file = File(context.filesDir, fileName)
            file.writeText(text)
            RepositoryItem(
                fileName = safeTitle,
                text = text,
                timestamp = timestamp
            )
        }
    }

}