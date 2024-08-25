package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // retrieves button text value that started the intent
        val buttonText = intent.getStringExtra("button_text")

        // sets the text to be the text of button that started the intent
        findViewById<TextView>(R.id.textView).apply {
            text = buttonText
        }
    }
}