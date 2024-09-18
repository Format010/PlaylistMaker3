package com.example.playlistmaker2.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track
import layout.SearchHolder

class SearchAdapter(
    var data: List<Track>,
    private val listenerItem: OnClickListenerItem
    ): RecyclerView.Adapter<SearchHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
            return SearchHolder(view, listenerItem)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: SearchHolder, position: Int) {
            holder.bind(data[position])
        }

        fun interface OnClickListenerItem {
            fun onItemClick(track: Track)
        }
    }