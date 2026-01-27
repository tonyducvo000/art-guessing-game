package com.example.artguess.data

import com.example.artguess.data.met.MetArtSource

object ArtRepository {

    private val artSource = MetArtSource()

    fun artWorkSize(): Int = 10

    suspend fun nextRound(excludeIds: Set<String> = emptySet(), numChoices: Int = 4): Round {
        return artSource.nextRound(excludeIds, numChoices)
    }

    suspend fun getRandomArtwork(): Artwork {
        return artSource.nextRound(emptySet(), 1).artwork
    }
}
