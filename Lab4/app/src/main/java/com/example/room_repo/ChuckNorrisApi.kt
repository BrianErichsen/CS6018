package com.example.room_repo

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory

//data class holds information - value is the actual joke
//maps value to joke in Json file
data class ChuckNorrisJoke(val value: String)

interface ChuckNorrisApi {
    //get random joke method performs a http get request to https://api.chucknorris.io/jokes/random
    @GET("jokes/random")
    //co-routine can be paused or resumed - so we don't freeze UI
    // returns instance of data class when request is successful
    suspend fun getRandomJoke(): ChuckNorrisJoke
    //class object that behaves like static method
    companion object {
        //creates instance without needing an object of itself
        fun create(): ChuckNorrisApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.chucknorris.io/")
                .addConverterFactory(GsonConverterFactory.create())//converts json data to kotlin object
                .build()
            return retrofit.create(ChuckNorrisApi::class.java)//generates implementation of interface at runtime
        }
    }
}