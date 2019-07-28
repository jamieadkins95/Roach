package com.jamieadkins.gwent.domain.tracker.predictions

import io.reactivex.Single

interface CardPredictorRepository {

    fun getPredictions(leaderId: String, cardIds: List<String>): Single<CardPredictions>
}