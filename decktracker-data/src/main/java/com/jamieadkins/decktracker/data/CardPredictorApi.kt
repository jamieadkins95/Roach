package com.jamieadkins.decktracker.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Base URL: https://us-central1-gwent-9e62a.cloudfunctions.net/
 */
interface CardPredictorApi {

    /**
     * @param cardIds comma separated card ids. e.g. "123,534,6541"
     */
    @GET("analyseDeck")
    fun analyseDeck(
        @Query("leaderId") leaderId: String,
        @Query("cardIds") cardIds: String
    ): Single<CardPredictorResponse>
}