package com.example.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MotionEvent
import android.widget.*
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var frame: LinearLayout
    private lateinit var player: ImageView
    private lateinit var orb: ImageView
    private lateinit var textTimer: TextView
    private lateinit var textScore: TextView

    private var isMoving = false
    private var timer = Timer()
    private var handler = Handler()

    private var targetX = 0.0F
    private var targetY = 0.0F

    private val minSpeed = 3.0
    private val maxSpeed = 30.0
    private val boost = 0.005
    private var speed = minSpeed

    private var secondsLeft = 60
    private var totalScore = 0
    private val scoreForOrb = 100

    private lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        nickname = intent.extras!!.getString("nickname").toString()

        findViews()
        textScore.text = "$totalScore"

        frame.setOnTouchListener { _, event ->
            when(event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    isMoving = true
                    targetX = event.x - player.width / 2
                    targetY = event.y - player.height / 2
                }
                MotionEvent.ACTION_MOVE -> {
                    isMoving = true
                    targetX = event.x - player.width / 2
                    targetY = event.y - player.height / 2
                }
            }
            true
        }

        timer.schedule(object: TimerTask() {
            override fun run() {
                handler.post { move() }
            }
        }, 0, 10)

        startTimeCounter()
    }

    private fun startTimeCounter() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft--
                textTimer.text = "$secondsLeft s"
            }

            override fun onFinish() {
                isMoving = false
                finish()
            }
        }.start()
    }

    private fun findViews() {
        frame = findViewById(R.id.frame)
        player = findViewById(R.id.player)
        orb = findViewById(R.id.orb)
        textTimer = findViewById(R.id.text_timer)
        textScore = findViewById(R.id.text_score)
    }

    private fun orbSpawn() {
        val padding = 30
        val screenX = frame.measuredWidth - padding * 2
        val screenY = frame.measuredHeight - padding * 2
        var newX = Random.nextFloat() * screenX + padding
        while((newX > player.x) and (newX < player.x + player.width))
            newX = Random.nextFloat() * screenX + padding
        var newY = Random.nextFloat() * screenY + padding
        while((newY > player.y) and (newY < player.y + player.height))
            newY = Random.nextFloat() * screenY + padding
        orb.x = newX
        orb.y = newY
    }

    private fun move() {
        if ((player.x == targetX) and (player.y == targetY)) {
            isMoving = false
        }
        if (!isMoving) {
            return
        }
        speed += boost
        if (speed > maxSpeed) speed = maxSpeed
        val distance: Double = sqrt((targetX - player.x.toDouble()).pow(2.0) + (targetY - player.y.toDouble()).pow(2))
        val sinVal = (targetY - player.y).toDouble() / distance
        val cosVal = (targetX - player.x).toDouble() / distance
        player.x += (speed * cosVal).toFloat()
        player.y += (speed * sinVal).toFloat()
        if (abs(player.x - targetX) < speed && abs(player.y - targetY) < speed) {
            player.x = targetX
            player.y = targetY
        }
        if ((player.x < orb.x + orb.width / 2) and (orb.x + orb.width / 2 < (player.x + player.width))
                and (player.y < orb.y + orb.height / 2) and (orb.y + orb.height / 2 < (player.y + player.height))) {
            totalScore += scoreForOrb
            textScore.text = "$totalScore"
            orbSpawn()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(applicationContext, ResultActivity::class.java)
        intent.putExtra("score", totalScore)
        intent.putExtra("nickname", nickname)
        startActivity(intent)
    }
}
