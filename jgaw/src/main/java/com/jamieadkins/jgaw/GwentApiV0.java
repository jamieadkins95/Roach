package com.jamieadkins.jgaw;

import com.jamieadkins.jgaw.Card.Card;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit api for gwentapi.com
 */

public interface GwentApiV0 {
    @GET("cards")
    Observable<CardListResult> listCards();

    @GET("cards/{cardId}")
    Observable<Card> getCard(@Path("cardId") String cardId);

    @GET("cards/leaders")
    Observable<CardListResult> getLeaders();

    @GET("cards/factions/{factionId}")
    Observable<CardListResult> getCardsFromFaction(@Path("factionId") String faction);

    @GET("cards/rarities/{rarity}")
    Observable<CardListResult> getCardsOfRarity(@Path("rarity") String rarity);
}
