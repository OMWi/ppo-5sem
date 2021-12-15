package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

class PlayerActivity : AppCompatActivity() {
    private lateinit var playerPosition: TextView
    private lateinit var playerDuration: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btRewind: ImageView
    private lateinit var btPrev: ImageView
    private lateinit var btPlay: ImageView
    private lateinit var btPause: ImageView
    private lateinit var btNext: ImageView
    private lateinit var btForward: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerPosition = findViewById(R.id.player_position)
        playerDuration = findViewById(R.id.player_duration)
        seekBar = findViewById(R.id.seek_bar)
        btRewind = findViewById(R.id.bt_rewind)
        btPrev = findViewById(R.id.bt_prev)
        btPlay = findViewById(R.id.bt_play)
        btPause = findViewById(R.id.bt_pause)
        btNext = findViewById(R.id.bt_next)
        btForward = findViewById(R.id.bt_forward)
    }
}