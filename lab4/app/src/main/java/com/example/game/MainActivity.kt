package com.example.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
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

    private var isLogin = true

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
            if (isLogin) {
                val intent = Intent(applicationContext, GameActivity::class.java)
                intent.putExtra("nickname", textName.text)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Login is required", Toast.LENGTH_SHORT).show()
            }
        }
        buttonRating.setOnClickListener{
            val intent = Intent(applicationContext, RatingActivity::class.java)
            startActivity(intent)
        }
        userPanel.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}