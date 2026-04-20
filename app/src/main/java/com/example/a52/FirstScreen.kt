package com.example.a52

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun FirstScreen(viewModel: ReworkViewModel, modifier: Modifier = Modifier) {
    val reposState = viewModel.repos.collectAsState()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("Фото добавлено в галерею")
            }
            viewModel.refresh()
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
        if (granted) {
            val file = viewModel.createImageFile()
            if (file != null) {
                val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                takePictureLauncher.launch(uri)
            } else {
                coroutineScope.launch { snackBarHostState.showSnackbar("Не удалось создать файл для фото") }
            }
        } else {
            Log.d("FirstScreen", "Camera permission denied")
            coroutineScope.launch { snackBarHostState.showSnackbar("Разрешение на камеру не предоставлено") }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Column(modifier = modifier
            .padding(innerPadding)
            .padding(top = 60.dp, start = 16.dp, end = 16.dp).navigationBarsPadding() // учёт нижней панели для всей колонки
        ) {
            val gridState = rememberLazyGridState()
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (reposState.value.isEmpty()) {
                    item(span = { GridItemSpan(3) }) {
                        Text(
                            text = "У вас пока нет фото. Сделайте первую фотку",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(items = reposState.value, key = { it.name }) { note ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            val fileName = note.name
                            val file = File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), fileName)
                            AsyncImage(
                                model = file.absolutePath,
                                contentDescription = note.name,
                                modifier = Modifier.size(112.dp).padding(4.dp)
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    // check permission
                    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        val file = viewModel.createImageFile()
                        if (file != null) {
                            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            takePictureLauncher.launch(uri)
                        } else {
                            coroutineScope.launch { snackBarHostState.showSnackbar("Не удалось создать файл для фото") }
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding().padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
            ) {
                Text("Сделать фотку")
            }
        }
    }
}