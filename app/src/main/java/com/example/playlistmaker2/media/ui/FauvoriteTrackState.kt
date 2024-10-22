package com.example.playlistmaker2.media.ui

import com.example.playlistmaker2.search.domain.model.Track

sealed interface FauvoriteTrackState {

        object Loading : FauvoriteTrackState

        data class Content(
            var data: List<Track>,
        ) : FauvoriteTrackState

        object Empty: FauvoriteTrackState

    }
