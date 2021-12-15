package com.example.audioplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import java.io.File

class VideoActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView

    private lateinit var video: Video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.video_view)

        video = intent.extras!!.get("video") as Video

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val uri = Uri.fromFile(File(video.path))
        videoView.setVideoURI(uri)
        videoView.start()
    }
}