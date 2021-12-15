package com.example.audioplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception

class PlayerActivity : AppCompatActivity() {
    private lateinit var playerPosition: TextView
    private lateinit var playerDuration: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btRewind: ImageView
    private lateinit var btPrev: ImageView
    private lateinit var btPlay: ImageView
    private lateinit var btNext: ImageView
    private lateinit var btForward: ImageView
    private lateinit var textTitle: TextView
    private lateinit var textArtist: TextView

    private lateinit var audioList: ArrayList<Audio>
    private var REQUEST_CODE = 1
    private var position = -1
    private lateinit var uri: Uri
//    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerPosition = findViewById(R.id.player_position)
        playerDuration = findViewById(R.id.player_duration)
        seekBar = findViewById(R.id.seek_bar)
        btRewind = findViewById(R.id.bt_rewind)
        btPrev = findViewById(R.id.bt_prev)
        btPlay = findViewById(R.id.bt_play)
        btNext = findViewById(R.id.bt_next)
        btForward = findViewById(R.id.bt_forward)
        textTitle = findViewById(R.id.text_title)
        textArtist = findViewById(R.id.text_artist)

        audioList = intent.extras!!.get("audio_list") as ArrayList<Audio>
        position = intent.extras!!.getInt("position")

        textTitle.text = audioList[position].title
        textArtist.text = audioList[position].artist

        btPlay.setImageResource(R.drawable.ic_pause)
        permission()

        try {
            uri = Uri.fromFile(File(audioList[position].path))
            val mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer!!.start()
        }
        catch (e: Exception) {
            print(e.message)
            print(e.stackTrace)
        }


    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE);
        }
        else {
            Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                permission()
            }
        }
    }
}