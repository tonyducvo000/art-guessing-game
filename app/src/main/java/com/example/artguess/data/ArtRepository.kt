package com.example.artguess.data

object ArtRepository {

    private val artworks = listOf(
        Artwork(
            id = "monet-1",
            title = "Impression, Sunrise",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0a/Claude_Monet%2C_Impression%2C_soleil_levant.jpg",
            artist = "Claude Monet"
        ),
        Artwork(
            id = "vermeer-1",
            title = "Girl with a Pearl Earring",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7f/Meisje_met_de_parel.jpg",
            artist = "Johannes Vermeer"
        ),
        Artwork(
            id = "munch-1",
            title = "The Scream",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/f/f4/The_Scream.jpg",
            artist = "Edvard Munch"
        ),
        Artwork(
            id = "hokusai-1",
            title = "The Great Wave off Kanagawa",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0a/Great_Wave_off_Kanagawa2.jpg",
            artist = "Hokusai"
        )
    )

    fun nextRound(excludeId: String? = null, numChoices: Int = 4): Round {
        val pool = if (excludeId == null) artworks else artworks.filter { it.id != excludeId }
        val artwork = pool.random()

        val decoys = artworks
            .map { it.artist }
            .distinct()
            .filter { it != artwork.artist }
            .shuffled()
            .take((numChoices - 1).coerceAtLeast(0))

        val choices = (listOf(artwork.artist) + decoys).shuffled()
        return Round(artwork, choices)
    }
}
