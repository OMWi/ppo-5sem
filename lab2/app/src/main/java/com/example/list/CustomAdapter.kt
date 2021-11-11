package com.example.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

class CustomAdapter(_context: Context, _results: MutableList<Enrollee>) : BaseAdapter(), Filterable {
    private var results = _results
    private var filteredResults = _results
    private var context = _context
    private var inflater = LayoutInflater.from(context)
    private var mFilter = ItemFilter()

    override fun getFilter(): Filter {
        return mFilter
    }

    override fun getCount(): Int {
        return filteredResults.size
    }

    override fun getItem(position: Int): Any {
        return filteredResults[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        var viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        }
        else {
            viewHolder = convertView.tag as ViewHolder
        }
        convertView as View

        var enrollee = filteredResults[position]
        viewHolder.textInitials.text = "${context.resources.getString(R.string.initials)}: ${enrollee.initials}"
        viewHolder.textAverageGrade.text = "${context.resources.getString(R.string.average_grade)}: %.2f".format(enrollee.averageGrade)
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

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterValue = constraint.toString().toDouble()
            val filterResults = FilterResults()
            val list = results
            val count = list.size
            val newList = mutableListOf<Enrollee>()
            var grade: Double
            for (i in 0 until count) {
                grade = list[i].averageGrade
                if (grade >= filterValue) {
                    newList.add(list[i])
                }
            }
            filterResults.values = newList.sortedByDescending { it.averageGrade }
            filterResults.count = newList.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, _results: FilterResults?) {
            filteredResults = _results!!.values as MutableList<Enrollee>
            notifyDataSetChanged()
        }
    }
}