package com.example.artguess.data.met

data class MetSearchResponse(
    val total: Int = 0,
    val objectIDs: List<Int>? = null
)

data class MetObjectResponse(
    val objectID: Int,
    val isPublicDomain: Boolean,
    val primaryImageSmall: String?,
    val title: String?,
    val artistDisplayName: String?,
    val objectEndDate: Int?
)
