package com.example.room_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Data access Object -- defines querying, inserting, updating, deleting
@Dao
interface JokeDao {
    @Insert
    suspend fun insert(joke: Joke) //inserts new row into jokes table - maps Joke object - columns
    // in the table

    //retrieves all rows
    @Query("SELECT * FROM jokes")
    suspend fun getAllJokes(): List<Joke>
}