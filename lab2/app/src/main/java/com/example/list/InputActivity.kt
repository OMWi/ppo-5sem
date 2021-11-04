package com.example.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class InputActivity : AppCompatActivity() {
    private lateinit var buttonInput: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val bundle = intent.extras
        val action: String = (bundle?.get("action") ?: resources.getString(R.string.add)) as String

        buttonInput = findViewById(R.id.button_input)
        buttonInput.text = action
    }

    fun onClickInput(v: View) {
        v as Button
    }

}