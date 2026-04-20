package com.example.a52

import java.io.File

class CreateImageFileUseCase(private val repository: Repository) {
    operator fun invoke(): File? = repository.createImageFile()
}

