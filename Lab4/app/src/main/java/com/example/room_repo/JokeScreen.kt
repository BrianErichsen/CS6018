package com.example.room_repo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.unit.dp
import com.example.room_repo.JokeViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text


@Composable
fun JokeScreen(jokeViewModel: JokeViewModel = viewModel()) {
    //collect as State converts stateflow into a compose
    val jokes by jokeViewModel.jokes.collectAsState()
    val currentJoke by jokeViewModel.currentJoke.collectAsState()

    //arranges data vertically
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { jokeViewModel.fetchRandomJoke() }) {
            Text(text = "Get Random Chuck Norris Joke")
        }
        Spacer(modifier = Modifier.height(16.dp))

        currentJoke?.let {
            Text(text = "Current Joke: ${it.jokeText}")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(jokes.size) { index->
                Text(text = jokes[index].jokeText)
            }
        }
    }
}