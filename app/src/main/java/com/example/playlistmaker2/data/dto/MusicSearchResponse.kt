package com.example.playlistmaker2.data.dto

import com.example.playlistmaker2.domain.models.Track
import java.util.LinkedList

class MusicSearchResponse(val searchType: String,
                          val expression: String,
                          val results: LinkedList<Track>
) : Response()