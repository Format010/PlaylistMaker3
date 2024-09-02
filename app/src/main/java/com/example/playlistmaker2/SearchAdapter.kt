package com.example.playlistmaker2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.domain.models.Track
import layout.SearchHolder
import java.util.LinkedList

class SearchAdapter(
    private val data: LinkedList<Track>, private val searchHistory: SearchHistory
    ): RecyclerView.Adapter<SearchHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
            return SearchHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: SearchHolder, position: Int) {
            holder.bind(data[position])

        }

}