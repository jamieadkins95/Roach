package com.jamieadkins.gwent.domain.latest

import io.reactivex.Observable

interface DeckOfTheDayRepository {

    fun getRandomDeckOfTheDay(): Observable<DeckOfTheDay>
}