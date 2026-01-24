package com.example.artguess.data.met

import com.example.artguess.data.Artwork
import com.example.artguess.data.Round
import kotlin.random.Random

class MetArtSource(
    private val api: MetApi = MetClient.api
) {
    // Cache IDs so you don't search every round
    private var cachedIds: List<Int> = emptyList()

    suspend fun nextRound(excludeIds: Set<String>, numChoices: Int = 4): Round {
        if (cachedIds.isEmpty()) {
            val search = api.search(q = "art", hasImages = true)
            cachedIds = search.objectIDs.orEmpty()
        }

        // Try a few times to find a usable object (public domain + has image + not seen)
        repeat(20) {
            if (cachedIds.isEmpty()) return fallbackRound()

            val id = cachedIds[Random.nextInt(cachedIds.size)]
            val obj = api.objectById(id)

            val imageUrl = obj.primaryImageSmall?.trim().orEmpty()
            val artist = obj.artistDisplayName?.trim().orEmpty()
            val title = obj.title?.trim().orEmpty()

            val appId = "met:$id"
            val usable =
                obj.isPublicDomain &&
                        imageUrl.isNotEmpty() &&
                        artist.isNotEmpty() &&
                        title.isNotEmpty() &&
                        appId !in excludeIds

            if (usable) {
                val artwork = Artwork(
                    id = appId,
                    title = title,
                    imageUrl = imageUrl,
                    artist = artist
                )

                // For decoys we need other artists; simplest: sample other objects
                val decoys = mutableSetOf<String>()
                while (decoys.size < (numChoices - 1).coerceAtLeast(0)) {
                    val decoyId = cachedIds[Random.nextInt(cachedIds.size)]
                    val decoyObj = api.objectById(decoyId)
                    val decoyArtist = decoyObj.artistDisplayName?.trim().orEmpty()
                    if (decoyArtist.isNotEmpty() && decoyArtist != artist) decoys += decoyArtist
                }

                val choices = (listOf(artist) + decoys.toList()).shuffled()
                return Round(artwork = artwork, choices = choices)
            }
        }

        // If we couldn't find a usable one quickly
        return fallbackRound()
    }

    private fun fallbackRound(): Round {
        // You can return a local hardcoded round here so the game never dies.
        // For now, throw so you see it in Logcat.
        throw IllegalStateException("MetArtSource: no usable public-domain image found.")
    }
}
