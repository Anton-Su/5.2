package com.example.a52

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.a52.ui.theme._52Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = DataTxtSource(applicationContext)
        val repository = RepositoryImpl(dataSource)
        val getTodosUseCase = GetAllUseCase(repository)
        val createImageFileUseCase = CreateImageFileUseCase(repository)
        val viewModel = ReworkViewModel(getTodosUseCase, createImageFileUseCase)
        enableEdgeToEdge()
        setContent {
            _52Theme {
                FirstScreen(viewModel = viewModel)
            }
        }
    }
}
