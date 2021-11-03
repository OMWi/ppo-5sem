package com.example.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private var list = mutableListOf<Enrollee>()
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter

    private fun addEnrollee(i: Int) {
        list.add(Enrollee("Initials$i", arrayOf(1, 2, 3)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..5) addEnrollee(i)
        listView = findViewById(R.id.listView)
        adapter = CustomAdapter(this, list)
        listView.adapter = adapter
    }
}