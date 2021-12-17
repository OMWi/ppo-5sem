package com.example.game

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var editName: EditText
//    private lateinit var editNickname: EditText
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
            val name = editName.text.toString()
            
            val cur = DBHelper(this, null).getByName(name)
            if (cur!!.count > 0) {
                Toast.makeText(applicationContext, "Name already taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            


            val intent = Intent()
            intent.putExtra("name", name)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun findViews() {
        editName = findViewById(R.id.edit_name)
//        editNickname = findViewById(R.id.edit_nickname)
        buttonConfirm = findViewById(R.id.button_confirm)
    }
}