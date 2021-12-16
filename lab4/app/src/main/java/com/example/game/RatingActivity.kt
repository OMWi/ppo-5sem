package com.example.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class RatingActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    private lateinit var ratingList: ArrayList<Rating>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        ratingList = intent.extras!!.get("rating_list") as ArrayList<Rating>
        val adapter = ArrayAdapter<Rating>(this, android.R.layout.simple_list_item_1, ratingList)
        listView.adapter = adapter

        findViews()
    }

    private fun findViews() {
        listView = findViewById(R.id.rating_list)
    }
}