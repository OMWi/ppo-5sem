package com.example.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var textName: TextView
    private lateinit var labelRating: TextView
    private lateinit var textRating: TextView
    private lateinit var buttonPlay: Button
    private lateinit var buttonRating: Button
    private lateinit var userPanel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        addListeners()
    }

    private fun findViews() {
        textName = findViewById(R.id.text_menu_name)
        labelRating = findViewById(R.id.label_menu_rating)
        textRating = findViewById(R.id.text_menu_rating)
        buttonPlay = findViewById(R.id.button_play)
        buttonRating = findViewById(R.id.button_rating)
        userPanel = findViewById(R.id.user_panel)
    }

    private fun addListeners() {
        buttonPlay.setOnClickListener {
            Toast.makeText(this, "Button play", Toast.LENGTH_SHORT).show()
        }
        buttonRating.setOnClickListener{
            Toast.makeText(this, "Button rating", Toast.LENGTH_SHORT).show()
        }
        userPanel.setOnClickListener {
            Toast.makeText(this, "User panel", Toast.LENGTH_SHORT).show()
        }
    }


}