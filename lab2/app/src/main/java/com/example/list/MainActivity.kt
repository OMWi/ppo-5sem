package com.example.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var list = mutableListOf<Enrollee>()
    private lateinit var textView: TextView
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter
    private var itemPosition = -1

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
        registerForContextMenu(listView)

        //parent - listView; view - layout; position, id - list item index
        /*
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
         */
    }

    //context menu funcs
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.context_menu, menu)
        itemPosition = (menuInfo as AdapterView.AdapterContextMenuInfo).position
        return super.onCreateContextMenu(menu, v, menuInfo)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                list.removeAt(itemPosition)
                adapter.notifyDataSetChanged()
                true
            }
            R.id.menu_edit -> {
                Toast.makeText(this, "edit $itemPosition", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    //options menu funcs
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_add -> {
                Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
                var intent = Intent(applicationContext, InputActivity::class.java)
                intent.putExtra("action", resources.getString(R.string.add))
                if (intent != null) startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }



}