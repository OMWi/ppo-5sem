package com.example.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editNickname: EditText
    private lateinit var buttonConfirm: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViews()
        buttonConfirm.setOnClickListener { 
            if (editName.text.isEmpty()) {
                Toast.makeText(applicationContext, "Enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (editNickname.text.isEmpty()) {
                Toast.makeText(applicationContext, "Enter your nickname", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // check if nickname taken
            val isTaken = false
            if (isTaken) {
                Toast.makeText(applicationContext, "Nickname already taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // send result to main activity
        }
    }

    private fun findViews() {
        editName = findViewById(R.id.edit_name)
        editNickname = findViewById(R.id.edit_nickname)
        buttonConfirm = findViewById(R.id.button_confirm)
    }
}