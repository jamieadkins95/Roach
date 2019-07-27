package com.jamieadkins.decktracker.data

import com.google.gson.annotations.SerializedName

data class CardPredictorResponse(
    @SerializedName("similarDecks") val similarDecks: Int?,
    @SerializedName("probabilities") val probabilities: Map<Long, Float>?
)