package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class VideoActivity : AppCompatActivity() {
    private var position = -1
    private lateinit var videoList: ArrayList<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoList = intent.extras!!.get("video_list") as ArrayList<Video>
        position = intent.extras!!.getInt("position")


    }
}