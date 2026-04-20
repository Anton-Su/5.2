package com.example.a52

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataTxtSource(private val context: Context) {
    suspend fun getRepos(): List<RepositoryItem> {
        return withContext(Dispatchers.IO) {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val files = dir?.listFiles()
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault())
            files
                ?.filter { it.extension.equals("jpg", true) || it.extension.equals("jpeg", true) || it.extension.equals("png", true) }
                ?.map { file ->
                    val nameNoExt = file.nameWithoutExtension
                    val ts = try {
                        if (nameNoExt.startsWith("IMG_")) {
                            val datePart = nameNoExt.removePrefix("IMG_")
                            sdf.parse(datePart)?.time ?: file.lastModified()
                        } else {
                            file.lastModified()
                        }
                    } catch (e: Exception) {
                        file.lastModified()
                    }
                    Pair(file, ts)
                }
                ?.sortedByDescending { it.second }
                ?.map { (file, _) ->
                    RepositoryItem(name = file.name)
                }
                ?: emptyList()
        }
    }

    fun createImageFile(): File? {
        return try {
            val timestamp = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault())
            val formatted = sdf.format(Date(timestamp))
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "IMG_${formatted}.jpg")
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}