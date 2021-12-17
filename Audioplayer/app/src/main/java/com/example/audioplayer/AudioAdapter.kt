package com.example.audioplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AudioAdapter(_context: Context, _results: MutableList<Audio>) : BaseAdapter(){
    private var results = _results
    private var context = _context
    private var inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(position: Int): Any {
        return results[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.audio_item, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        }
        else {
            viewHolder = convertView.tag as ViewHolder
        }
        convertView as View

        val audio = results[position]
        viewHolder.textArtist.text = audio.artist
        viewHolder.textTitle.text = audio.title
        return convertView
    }

    private class ViewHolder(view: View) {
        var textTitle: TextView = view.findViewById(R.id.audio_item_title)
        var textArtist: TextView = view.findViewById(R.id.audio_item_artist)
    }
}