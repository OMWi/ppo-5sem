package com.example.audioplayer

import java.io.Serializable

data class Audio(
    val path: String,
    val title: String,
    val artist: String,
    val duration: String
) : Serializable