package com.example.room_repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//this class creates instance of database and provides the DAO
// lists all tables - just the Joke table
@Database(entities = [Joke::class], version = 1)
abstract class AppDatabase : RoomDatabase() { // abstract since we want implementation at runtime
    //and implementation for its sub-classes // no objects for abstract classes
    abstract fun jokeDao(): JokeDao //Room will generate implementation of jokeDao at compile

    //volatile -> changes instance variable -- visible to all threads
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //only 1 creating of database at a time - synch
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder( //creates Room database instance
                    context.applicationContext, //params are context, which class and name
                    AppDatabase::class.java,
                    "jokes_database")
                    .build()
                    INSTANCE = instance
                    instance
            }
        }
    }
}