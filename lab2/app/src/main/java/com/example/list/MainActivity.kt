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
    private var totalAverage = 0.0
    private var isFiltered = false

    private fun addEnrollee(i: Int) {
        list.add(Enrollee("Surname$i Name$i", arrayOf(i, i, i)))
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
        onListChange()
    }

    private fun onListChange() {
        if (list.isNotEmpty()) {
            totalAverage = list.sumOf { it.averageGrade } / list.size
            textView.text = "${resources.getString(R.string.average_grade)} $totalAverage"
        }
        else {
            textView.text = "${resources.getString(R.string.average_grade)}"
        }
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
                val enrollee = adapter.getItem(itemPosition) as Enrollee
                list.remove(enrollee)
                onListChange()
                if (isFiltered) {
                    adapter.filter.filter(totalAverage.toString())
                }
                else {
                    adapter.filter.filter("-1")
                }
                true
            }
            R.id.menu_edit -> {
                val intent = Intent(applicationContext, InputActivity::class.java)
                intent.putExtra("action", resources.getString(R.string.edit))
                intent.putExtra("enrollee", adapter.getItem(itemPosition) as Enrollee)
                startActivityForResult(intent, 1)
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
                val intent = Intent(applicationContext, InputActivity::class.java)
                intent.putExtra("action", resources.getString(R.string.add))
                startActivityForResult(intent, 0)
                true
            }
            R.id.menu_filter -> {
                item.title = when(isFiltered) {
                    false -> {
                        adapter.filter.filter(totalAverage.toString())
                        resources.getString(R.string.disable_filter)
                    }
                    else -> {
                        adapter.filter.filter("-1")
                        resources.getString(R.string.enable_filter)
                    }

                }
                isFiltered = !isFiltered
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode) {
            0 -> {
                val newEnrollee: Enrollee = data!!.extras!!.get("enrollee") as Enrollee
                list.add(newEnrollee)
                adapter.notifyDataSetChanged()
                onListChange()
                if (isFiltered) {
                    adapter.filter.filter(totalAverage.toString())
                }
            }
            1 -> {
                val editedEnrollee: Enrollee = data!!.extras!!.get("enrollee") as Enrollee
                var oldEnrollee = list.find{ it.id == editedEnrollee.id }
                oldEnrollee!!.initials = editedEnrollee.initials
                oldEnrollee.grades = editedEnrollee.grades
                oldEnrollee.averageGrade = editedEnrollee.averageGrade
                adapter.notifyDataSetChanged()
                onListChange()
                if (isFiltered) {
                    adapter.filter.filter(totalAverage.toString())
                }
            }
        }
    }
}