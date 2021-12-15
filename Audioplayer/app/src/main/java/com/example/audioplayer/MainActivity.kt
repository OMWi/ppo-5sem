package com.example.audioplayer

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var playerPosition: TextView
    private lateinit var playerDuration: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btRewind: ImageView
    private lateinit var btPrev: ImageView
    private lateinit var btPlay: ImageView
    private lateinit var btPause: ImageView
    private lateinit var btNext: ImageView
    private lateinit var btForward: ImageView

    private var REQUEST_CODE = 1
    private var audioList = mutableListOf<Audio>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerPosition = findViewById(R.id.player_position)
        playerDuration = findViewById(R.id.player_duration)
        seekBar = findViewById(R.id.seek_bar)
        btRewind = findViewById(R.id.bt_rewind)
        btPrev = findViewById(R.id.bt_prev)
        btPlay = findViewById(R.id.bt_play)
        btPause = findViewById(R.id.bt_pause)
        btNext = findViewById(R.id.bt_next)
        btForward = findViewById(R.id.bt_forward)
        permission()
        getAudioList()
    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE);
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

    private fun getAudioList() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        val cursor = this.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(3)
                if (!path.contains("/MediaPlayer/Audio/"))
                    continue
                val title = cursor.getString(0)
                val artist = cursor.getString(1)
                val duration = cursor.getString(2)
                val audio = Audio(path, title, artist, duration)
                audioList.add(audio)
            }
            cursor.close()
        }
    }

}