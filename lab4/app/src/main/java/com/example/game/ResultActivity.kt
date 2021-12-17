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

    private lateinit var name: String
    private lateinit var score: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViews()

        val extras: Bundle? = intent.extras
        name = extras!!.get("nickname").toString()
        score = extras.get("score").toString()

        textNickname.text = name
        textScore.text = "Score $score"
//        buttonReplay.setOnClickListener {
//            val intent = Intent(applicationContext, GameActivity::class.java)
//            intent.putExtra("nickname", textNickname.text)
//            startActivity(intent)
//            finish()
//        }
    }

    private fun findViews() {
        textNickname = findViewById(R.id.res_nickname)
        textScore = findViewById(R.id.res_score)
//        buttonReplay = findViewById(R.id.button_res_replay)
    }
}