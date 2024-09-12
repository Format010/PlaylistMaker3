package com.example.playlistmaker2.search.data.dto

import com.example.playlistmaker2.search.domain.model.Track
import java.util.LinkedList

class SearchResponse(
    val results: List<Track>
) : Response()