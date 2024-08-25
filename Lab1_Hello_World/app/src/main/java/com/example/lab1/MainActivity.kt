package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener(this)
        findViewById<Button>(R.id.button2).setOnClickListener(this)
        findViewById<Button>(R.id.button3).setOnClickListener(this)
        findViewById<Button>(R.id.button4).setOnClickListener(this)
        findViewById<Button>(R.id.button5).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        // finds which button was clicked
        val clickedButton = v as Button
        val buttonText = clickedButton.text.toString()

        // creates a new intent before starting the second activity
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("button_text", buttonText) //adds button text to the intent
        }
        startActivity(intent)
    }
}//end of main class activity