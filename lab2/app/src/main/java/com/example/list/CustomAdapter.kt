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
    private var inflater = LayoutInflater.from(context)

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
        var viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        }
        else {
            viewHolder = convertView.tag as ViewHolder
        }
        convertView as View

        var enrollee = searchResults[position]
        viewHolder.textInitials.text = "${context.resources.getString(R.string.initials)}: ${enrollee.initials}"
        viewHolder.textAverageGrade.text = "${context.resources.getString(R.string.average_grade)}: ${enrollee.averageGrade}"
        var gradesStr = "${context.resources.getString(R.string.grades)}: "
        for (grade in enrollee.grades) {
            gradesStr += "$grade, "
        }
        gradesStr = gradesStr.dropLast(2)
        viewHolder.textGrades.text = gradesStr
        return convertView
    }

    private class ViewHolder(view: View) {
        var textInitials: TextView = view.findViewById(R.id.text_initials)
        var textGrades: TextView = view.findViewById(R.id.text_grades)
        var textAverageGrade: TextView = view.findViewById(R.id.text_averageGrade)
    }
}