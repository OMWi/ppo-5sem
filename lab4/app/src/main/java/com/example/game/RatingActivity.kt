package com.example.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class RatingActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var buttonSort: Button

    private var ratingList = mutableListOf<Rating>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        findViews()

        val tempList = intent.extras!!.get("rating_list") as ArrayList<Rating>
        for (elem in tempList) {
            ratingList.add(elem)
        }
        val adapter = RatingAdapter(applicationContext, ratingList)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()

        buttonSort.setOnClickListener {
            adapter.filter.filter("")
        }
    }

    private fun findViews() {
        listView = findViewById(R.id.rating_list)
        buttonSort = findViewById(R.id.button_sort)
    }
}