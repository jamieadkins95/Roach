package com.jamieadkins.decktracker.data

import com.google.gson.annotations.SerializedName

data class CardPredictorResponse(
    @SerializedName("similarDecks") val similarDecks: List<SimilarDeckResponse>?,
    @SerializedName("deckCount") val deckCountForLeader: Int?,
    @SerializedName("probabilities") val probabilities: Map<Long, Float>?
)

data class SimilarDeckResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("votes") val votes: Int?
)