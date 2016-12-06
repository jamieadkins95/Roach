package com.jamieadkins.jgaw;

import com.jamieadkins.jgaw.card.Card;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit api for gwentapi.com
 */

public interface GwentApiV0 {
    @GET("cards")
    Call<CardListResult> getAllCards();

    @GET("cards")
    Call<CardListResult> getAllCards(@Query("limit") String limit, @Query("offset") String offset);

    @GET("cards/{cardId}")
    Call<Card> getCard(@Path("cardId") String cardId);

    @GET("cards/leaders")
    Call<CardListResult> getLeaders();

    @GET("cards")
    Call<CardListResult> getLeaders(@Query("limit") String limit, @Query("offset") String offset);

    @GET("cards/factions/{factionId}")
    Call<CardListResult> getCardsFromFaction(@Path("factionId") String faction);

    @GET("cards/factions/{factionId}")
    Call<CardListResult> getCardsFromFaction(@Path("factionId") String faction,
                                             @Query("limit") String limit,
                                             @Query("offset") String offset);

    @GET("cards/rarities/{rarity}")
    Call<CardListResult> getCardsOfRarity(@Path("rarity") String rarity);

    @GET("cards/rarities/{rarity}")
    Call<CardListResult> getCardsOfRarity(@Path("rarity") String rarity,
                                          @Query("limit") String limit,
                                          @Query("offset") String offset);
}
