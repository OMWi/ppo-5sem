package com.example.audioplayer

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter

    private var REQUEST_PERMISSION = 1
    private var audioList = mutableListOf<Audio>()
    private var videoList = mutableListOf<Video>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.audio_list)
        adapter = CustomAdapter(this, audioList)
        listView.adapter = adapter

        permission()
        getAudioList()
        getVideoList()

        val intent = Intent(applicationContext, VideoActivity::class.java)
        intent.putExtra("video", videoList[0])
        startActivity(intent)



        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, AudioActivity::class.java)
            intent.putExtra("audio_list", audioList as ArrayList<Audio>)
            intent.putExtra("position", position)
            startActivity(intent)
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
        adapter.notifyDataSetChanged()
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
                print(path)
                if (!path.contains("/MediaPlayer/Video/"))
                    continue
                val title = cursor.getString(0)
                val duration = cursor.getString(1)
                val video = Video(path, title, duration)
                videoList.add(video)
            }
            cursor.close()
        }
    }

}