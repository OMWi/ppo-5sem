package com.example.audioplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception

class AudioActivity : AppCompatActivity() {
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
    private var mediaPlayer: MediaPlayer? = null
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        playerPosition = findViewById(R.id.audio_time)
        playerDuration = findViewById(R.id.audio_duration)
        seekBar = findViewById(R.id.audio_seekbar)
        btRewind = findViewById(R.id.audio_rewind)
        btPrev = findViewById(R.id.audio_prev)
        btPlay = findViewById(R.id.audio_play)
        btNext = findViewById(R.id.audio_next)
        btForward = findViewById(R.id.audio_forward)
        textTitle = findViewById(R.id.audio_title)
        textArtist = findViewById(R.id.audio_artist)

        audioList = intent.extras!!.get("audio_list") as ArrayList<Audio>
        position = intent.extras!!.getInt("position")

        textTitle.text = audioList[position].title
        textArtist.text = audioList[position].artist

        btPlay.setImageResource(R.drawable.ic_pause)

        uri = Uri.fromFile(File(audioList[position].path))
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        mediaPlayer!!.start()
        setView(audioList[position])
        playerPosition.text = formattedTime(mediaPlayer!!.currentPosition)
        playerDuration.text = formattedTime(mediaPlayer!!.duration)
        handler.postDelayed(runnable, 0)

        seekBar.max = mediaPlayer!!.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if ((mediaPlayer != null) and (fromUser)) {
                    mediaPlayer!!.pause()
                    mediaPlayer!!.seekTo(progress)
                    mediaPlayer!!.start()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        btPlay.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                btPlay.setImageResource(R.drawable.ic_play)
            }
            else {
                mediaPlayer!!.start()
                btPlay.setImageResource(R.drawable.ic_pause)
            }
        }

        btPrev.setOnClickListener {
            if (position == 0) {
                position = audioList.size - 1
            }
            else position -= 1
            changeAudio(position)
        }

        btNext.setOnClickListener {
            if (position == (audioList.size - 1)) {
                position = 0
            }
            else position += 1
            changeAudio(position)
        }

        btRewind.setOnClickListener {
            mediaPlayer!!.pause()
            mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition - 10000)
            mediaPlayer!!.start()
        }

        btForward.setOnClickListener {
            mediaPlayer!!.pause()
            mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition + 10000)
            mediaPlayer!!.start()
        }

        mediaPlayer!!.setOnCompletionListener (MediaPlayer.OnCompletionListener {
            if (position == audioList.size - 1) {
                position = 0
            } else position += 1
            changeAudio(position)
        })

        this.runOnUiThread(runnable)
    }

    private fun formattedTime(ms: Int): String {
        var sec = ms / 1000
        var min = ms / 1000 / 60
        sec -= min * 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun changeAudio(pos: Int) {
        val audio = audioList[pos]
        setView(audio)
        handler.removeCallbacks(runnable)
        mediaPlayer!!.reset()
        try {
            val uri = Uri.fromFile(File(audio.path))
            mediaPlayer!!.setDataSource(applicationContext, uri)
            mediaPlayer!!.prepare()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        seekBar.max = mediaPlayer!!.duration
        handler.postDelayed(runnable, 0)
        mediaPlayer!!.start()
        playerDuration.text = formattedTime(mediaPlayer!!.duration)
        btPlay.setImageResource(R.drawable.ic_pause)
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val curPos = mediaPlayer!!.currentPosition
            seekBar.progress = curPos
            playerPosition.text = formattedTime(curPos)
            handler.postDelayed(this, 100)
        }
    }

    private fun setView(audio: Audio) {
        textTitle.text = audio.title
        textArtist.text = audio.artist
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.pause()
        handler.removeCallbacks(runnable)
        mediaPlayer!!.release()
    }
}