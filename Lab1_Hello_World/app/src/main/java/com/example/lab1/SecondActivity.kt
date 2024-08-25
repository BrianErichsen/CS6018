package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.View
import android.content.Intent

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // retrieves button text value that started the intent
        val buttonText = intent.getStringExtra("button_text")

        // sets the text to be the text of button that started the intent
        findViewById<TextView>(R.id.textView).apply {
            text = buttonText
        }
        // I added for practice a go back button that goes back to the MainActivity from
        // the second activity
        findViewById<Button>(R.id.go_back).setOnClickListener(this)
    }//end of on create method

    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}//end of second activity class