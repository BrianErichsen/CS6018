package com.example.room_repo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State

//ViewModel survives screen rotations ... extends androidViewModel - is life cycle aware
//and gives access to application context -- needs context to access database
class JokeViewModel(application: Application) : AndroidViewModel(application) {

    private val jokeDao = AppDatabase.getDatabase(application).jokeDao()
    private val api = ChuckNorrisApi.create()

    //mutable -- handle streams of data asynchronously
    private val _jokes = MutableStateFlow<List<Joke>>(emptyList()) // reflects UI updates
    val jokes: StateFlow<List<Joke>> = _jokes // holds current state we display
    // which are jokes and current joke

    private val _currentJoke = MutableStateFlow<Joke?>(null)
    val currentJoke: StateFlow<Joke?> = _currentJoke

    init { //launches coroutine in viewModel scope -- if viewModel is cleared -- it cancels coroutine
        viewModelScope.launch {
            //loads previously stored jokes from database and sets to _jokes
            _jokes.value = jokeDao.getAllJokes()
        }
    }

    // when this is called - makes a network request to fetch a random joke from the API
    fun fetchRandomJoke() {
        viewModelScope.launch {
            val apiJoke = api.getRandomJoke()
            val joke = Joke(jokeText = apiJoke.value)
            _currentJoke.value = joke // sets current joke as the one fetched
            // adds to the list of jokes
            _jokes.value = _jokes.value + joke
            //inserts it to room database
            jokeDao.insert(joke)
        }
    }
}