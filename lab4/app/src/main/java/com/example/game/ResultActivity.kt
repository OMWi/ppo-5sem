package com.example.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    private lateinit var textNickname: TextView
    private lateinit var textScore: TextView
    private lateinit var buttonReplay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViews()

        textNickname.text = intent.extras!!.getString("nickname")
        textScore.text = intent.extras!!.getString("score")
        buttonReplay.setOnClickListener {
            val intent = Intent(applicationContext, GameActivity::class.java)
            intent.putExtra("nickname", textNickname.text)
            startActivity(intent)
            finish()
        }
    }

    private fun findViews() {
        textNickname = findViewById(R.id.res_nickname)
        textScore = findViewById(R.id.res_score)
        buttonReplay = findViewById(R.id.button_res_replay)
    }
}