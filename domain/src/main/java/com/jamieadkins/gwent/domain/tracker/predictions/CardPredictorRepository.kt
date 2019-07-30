package com.jamieadkins.gwent.domain.tracker.predictions

import io.reactivex.Maybe

interface CardPredictorRepository {

    fun getPredictions(leaderId: String, cardIds: List<String>): Maybe<CardPredictions>
}