package com.example.playlistmaker2.data.dto

import com.example.playlistmaker2.domain.models.Track

class MusicResponse(val results: List<Track>) : Response()