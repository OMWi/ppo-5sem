package com.example.audioplayer

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var audioListView: ListView
    private lateinit var videoListView: ListView
    private lateinit var buttonSwitch: Button

    private lateinit var audioAdapter: AudioAdapter
    private lateinit var videoAdapter: VideoAdapter

    private var REQUEST_PERMISSION = 1
    private var audioList = mutableListOf<Audio>()
    private var videoList = mutableListOf<Video>()

    private var curStatus = 0
    private val videoStatus = 1
    private val audioStatus = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSwitch = findViewById(R.id.button_switch)

        audioListView = findViewById(R.id.audio_list)
        audioAdapter = AudioAdapter(this, audioList)
        audioListView.adapter = audioAdapter

        videoListView = findViewById(R.id.video_list)
        videoAdapter = VideoAdapter(this, videoList)
        videoListView.adapter = videoAdapter

        permission()
        getAudioList()
        getVideoList()

        audioListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, AudioActivity::class.java)
            intent.putExtra("audio_list", audioList as ArrayList<Audio>)
            intent.putExtra("position", position)
            startActivity(intent)
        }

        videoListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, VideoActivity::class.java)
            intent.putExtra("video", videoList[position])
            startActivity(intent)
        }

        buttonSwitch.setOnClickListener {
            if (curStatus == audioStatus) {
                curStatus = videoStatus
                audioListView.visibility = View.GONE
                videoListView.visibility = View.VISIBLE
            }
            else if (curStatus == videoStatus) {
                curStatus = audioStatus
                videoListView.visibility = View.GONE
                audioListView.visibility = View.VISIBLE
            }
        }
    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
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
//                if (!path.contains("/MediaPlayer/Audio/"))
//                    continue
                val title = cursor.getString(0)
                val artist = cursor.getString(1)
                val duration = cursor.getString(2)
                val audio = Audio(path, title, artist, duration)
                audioList.add(audio)
            }
            cursor.close()
        }
        audioAdapter.notifyDataSetChanged()
    }

    private fun getVideoList() {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATA
        )
        val cursor = this.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(2)
//                if (!path.contains("/MediaPlayer/Video/"))
//                    continue
                val title = cursor.getString(0)
                val duration = cursor.getString(1)
                val video = Video(path, title, duration)
                videoList.add(video)
            }
            cursor.close()
        }
    }

}