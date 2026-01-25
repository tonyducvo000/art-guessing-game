package com.example.artguess.data

import com.example.artguess.data.met.MetArtSource

object ArtRepository {

    private val artSource = MetArtSource()

    // Since we are fetching from an API, we'll use a fixed size for the "game" session
    // or let the UI handle the "infinite" nature if we want. 
    // Let's set it to 10 for a standard game length.
    fun artWorkSize(): Int = 10

    suspend fun nextRound(excludeIds: Set<String> = emptySet(), numChoices: Int = 4): Round {
        return artSource.nextRound(excludeIds, numChoices)
    }
}
