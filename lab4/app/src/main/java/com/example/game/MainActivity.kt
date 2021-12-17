package com.example.game

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var textName: TextView
    private lateinit var labelRating: TextView
    private lateinit var textRating: TextView
    private lateinit var buttonPlay: Button
    private lateinit var buttonRating: Button
    private lateinit var userPanel: LinearLayout

    private var isLogin = false
    private lateinit var db: DBHelper
    private var scores = mutableListOf<Rating>()

    private val REQUEST_NAME = 1

    private lateinit var startLoginActivity: ActivityResultLauncher<Intent>
    private lateinit var startGameActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        addListeners()

        db = DBHelper(applicationContext, null)
        getScores()

        startLoginActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val extra: Bundle? = result.data!!.extras
                val name = extra!!.get("name")
                isLogin = true
                textName.text = name.toString()
                Toast.makeText(this, "Name $name", Toast.LENGTH_SHORT).show()
            }
        }

        startGameActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val extra: Bundle? = result.data!!.extras
                val name = extra!!.get("name").toString()
                val score = extra.get("score").toString()
                val newScore = Rating(name, score)
                scores.add(newScore)
                db.addScore(name, score)
            }
        }

    }

    private fun getScores() {
        val cur = db.getScores() ?: return
        while (cur.moveToNext()) {
            val name = cur.getString(1)
            val score = cur.getString(2)
            scores.add(Rating(name, score))
        }
        cur.close()
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
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("nickname", textName.text)
                startGameActivity.launch(intent)
            }
            else {
                Toast.makeText(this, "Login is required", Toast.LENGTH_SHORT).show()
            }
        }
        buttonRating.setOnClickListener{
            val intent = Intent(applicationContext, RatingActivity::class.java)
            intent.putExtra("rating_list", scores as ArrayList<Rating>)
            startActivity(intent)
        }
        userPanel.setOnClickListener {
            if (isLogin) {
                return@setOnClickListener
            }
            startLoginActivity.launch(Intent(this, LoginActivity::class.java))
        }
    }
}