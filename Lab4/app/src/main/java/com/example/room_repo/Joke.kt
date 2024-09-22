package com.example.room_repo

import androidx.room.Entity
import androidx.room.PrimaryKey

//This data class -- creates room database
//database entity
@Entity(tableName = "jokes")
data class Joke (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jokeText: String
)