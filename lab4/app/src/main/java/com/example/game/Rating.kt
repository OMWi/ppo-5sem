package com.example.game

import java.io.Serializable

data class Rating(
    val name: String,
    val nickname: String,
    val score: Int
) : Serializable