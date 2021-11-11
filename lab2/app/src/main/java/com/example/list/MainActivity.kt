package com.example.list

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private var list = mutableListOf<Enrollee>()
    private lateinit var textView: TextView
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter
    private var itemPosition = -1
    private var totalAverage = 0.0
    private var isFiltered = false
    private val filePath = "Enrollee"
    private val fileName = "file.json"
    private lateinit var manager: EnrolleeManager
    private val PICKFILECODE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = EnrolleeManager(File(getExternalFilesDir(filePath), fileName))
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
            textView.text = "${resources.getString(R.string.average_grade)} %.2f".format(totalAverage)
        }
        else {
            textView.text = "${resources.getString(R.string.average_grade)}"
        }
        adapter.notifyDataSetChanged()
    }

    //context menu functions
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

    //options menu functions
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
            R.id.menu_save -> {
                manager.save(list)

                Toast.makeText(this, resources.getString(R.string.data_saved), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_open -> {
                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.type = "application/json"
                chooseFile = Intent.createChooser(chooseFile, resources.getString(R.string.choose_file))
                startActivityForResult(chooseFile, PICKFILECODE)
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
        if (data == null) return
        when(requestCode) {
            0 -> {
                val newEnrollee: Enrollee = data!!.extras!!.get("enrollee") as Enrollee
                list.add(newEnrollee)
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
                onListChange()
                if (isFiltered) {
                    adapter.filter.filter(totalAverage.toString())
                }
            }
            PICKFILECODE -> {
                if (resultCode == -1) {
                    val uri = data.data
                    manager.read(list, contentResolver.openInputStream(uri!!))
                    onListChange()
                }
            }
        }
    }
}