package com.example.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

class RatingAdapter(_context: Context, _results: MutableList<Rating>) : BaseAdapter(),
    Filterable {
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
            convertView = inflater.inflate(R.layout.rating_item, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        }
        else {
            viewHolder = convertView.tag as ViewHolder
        }
        convertView as View

        var rating = filteredResults[position]
        viewHolder.textName.text = "${rating.name}"
        viewHolder.textNickname.text = "(${rating.nickname})"
        viewHolder.textScore.text = "Score: ${rating.score}"
        return convertView
    }

    private class ViewHolder(view: View) {
        var textName: TextView = view.findViewById(R.id.rating_item_name)
        var textScore: TextView = view.findViewById(R.id.rating_item_score)
        var textNickname: TextView = view.findViewById(R.id.rating_item_nickname)
    }

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = results.sortedByDescending { it.score }
            filterResults.count = results.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, _results: FilterResults?) {
            filteredResults = _results!!.values as MutableList<Rating>
            notifyDataSetChanged()
        }
    }
}