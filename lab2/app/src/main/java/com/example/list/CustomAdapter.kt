package com.example.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(context: Context, results: MutableList<Enrollee>) : BaseAdapter() {
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var searchResults = results

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
        convertView = mInflater.inflate(R.layout.row_item, null)
        val initials = convertView?.findViewById<TextView>(R.id.text_initials)
        val grades = convertView?.findViewById<TextView>(R.id.text_grades)
        val averageGrade = convertView?.findViewById<TextView>(R.id.text_averageGrade)
        initials!!.text = searchResults[position].initials
        averageGrade!!.text = searchResults[position].averageGrade.toString()
        var gradesStr = ""
        for (grade in searchResults[position].grades) {
            gradesStr += grade
        }
        grades!!.text = gradesStr
        return convertView
    }
}