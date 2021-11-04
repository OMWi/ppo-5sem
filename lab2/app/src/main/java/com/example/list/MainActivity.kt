package com.example.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var list = mutableListOf<Enrollee>()
    private lateinit var textView: TextView
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter

    private fun addEnrollee(i: Int) {
        list.add(Enrollee("Initials$i", arrayOf(1, 3, 5)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..5) addEnrollee(i)
        textView = findViewById(R.id.textView)
        listView = findViewById(R.id.listView)
        adapter = CustomAdapter(this, list)
        listView.adapter = adapter
        //parent - listView; view - layout; position, id - list item index
        listView.setOnItemLongClickListener { parent, view, position, id ->
            textView.text = "position=$position\nid=$id"
            try {
                list.add(position, Enrollee("Initials_copy", arrayOf(1, 1, 1)))
                adapter.notifyDataSetChanged()
            }
            catch (e: Exception) {
                textView.text = "${textView.text}\nerr:${e.message}"
            }
            return@setOnItemLongClickListener true
        }
    }
}