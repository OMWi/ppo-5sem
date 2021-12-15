package com.example.audioplayer

import java.io.Serializable

class Video(
    val path: String,
    val title: String,
    val duration: String
) : Serializable