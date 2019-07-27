package com.jamieadkins.gwent.domain.tracker.predictions

import com.jamieadkins.gwent.domain.GwentFaction
import io.reactivex.Single

interface CardPredictorRepository {

    fun getPredictions(faction: GwentFaction, leaderId: Long, cardIds: List<Long>): Single<CardPredictions>
}