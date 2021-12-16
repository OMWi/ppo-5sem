package com.example.game

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class GameActivity : AppCompatActivity() {
    private lateinit var frame: FrameLayout
    private lateinit var player: ImageView
    private lateinit var debug: TextView

    private lateinit var playerDrawable: Drawable
    private var isMoving = false
    private var timer = Timer()
    private var handler = Handler()

    private var targetX = 0.0F
    private var targetY = 0.0F

    private val minSpeed = 2.0
    private val maxSpeed = 50.0
    private val boost = 0.05
    private var speed = minSpeed




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        findViews()
        playerDrawable = resources.getDrawable(R.drawable.ic_player)

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
                else -> {
                    isMoving = false
                }
            }
            true
        }

        timer.schedule(object: TimerTask() {
            override fun run() {
                handler.post { move() }
            }
        }, 0, 10)
    }

    private fun findViews() {
        frame = findViewById(R.id.frame)
        player = findViewById(R.id.player)
        debug = findViewById(R.id.text_debug)
    }

    private fun move() {
        if ((player.x == targetX) and (player.y == targetY)) {
            isMoving = false
        }
        if (!isMoving) {
            speed = minSpeed
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
        debug.text = String.format("speed %.2f", speed)
    }
}
