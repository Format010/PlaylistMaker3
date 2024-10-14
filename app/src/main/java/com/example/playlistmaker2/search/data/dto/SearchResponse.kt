package com.example.playlistmaker2.search.data.dto

import com.example.playlistmaker2.search.domain.model.Track

class SearchResponse(
    val results: List<Track>
) : Response()