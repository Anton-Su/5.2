package com.example.a52.ui.theme

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.example.a52.ReworkViewModel


@Composable
fun FirstScreen(viewModel: ReworkViewModel, navHostController: NavHostController, modifier: Modifier = Modifier,) {
    val reposState = viewModel.repos.collectAsState()
    Column(modifier = Modifier
        .padding(top = 60.dp, start = 16.dp, end = 16.dp).navigationBarsPadding() // учёт нижней панели для всей колонки
    ) {
        LazyColumn(Modifier.weight(1f)) {
            if (reposState.value.isEmpty()) {
                item {
                    Text(
                        text = "У вас пока нет записей. Нажмите кнопку создать, чтобы создать первую заметку.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            else
            items(reposState.value) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth().padding(8.dp)
                        .combinedClickable(
                            onClick = {

                            },
                            onLongClick = {
                                viewModel.deletePrevItem(note)
                            }
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = note.fileName)
                        Text(text = note.text)
                        Text(text = viewModel.formatDate(note.timestamp))
                    }
                }
            }
        }
        Button(
            onClick = {
                navHostController.navigate(Screen.Detail.route)
            },
            modifier = Modifier
                .navigationBarsPadding().padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
        ) {
            Text("Создать")
        }
    }
}