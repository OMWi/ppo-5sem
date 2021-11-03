package com.example.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(_context: Context, results: MutableList<Enrollee>) : BaseAdapter() {
    private var searchResults = results
    private var context = _context

    override fun getCount(): Int {
        return searchResults.size
    }

    override fun getItem(position: Int): Any {
        return searchResults[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item, null)
        val initials = convertView?.findViewById<TextView>(R.id.text_initials)
        val grades = convertView?.findViewById<TextView>(R.id.text_grades)
        val averageGrade = convertView?.findViewById<TextView>(R.id.text_averageGrade)
        initials!!.text = "${context.resources.getString(R.string.initials)}: ${searchResults[position].initials}"
        averageGrade!!.text = "${context.resources.getString(R.string.average_grade)}: ${searchResults[position].averageGrade}"
        var gradesStr = "${context.resources.getString(R.string.grades)}: "
        for (grade in searchResults[position].grades) {
            gradesStr += "$grade, "
        }
        gradesStr = gradesStr.dropLast(2)
        grades!!.text = gradesStr
        return convertView
    }
}