package com.jamieadkins.gwent.data.card.mapper

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.card.model.FirebaseCardResult
import com.jamieadkins.gwent.database.entity.ArtEntity
import javax.inject.Inject

class ArtApiMapper @Inject constructor() : Mapper<FirebaseCardResult, Collection<ArtEntity>>() {

    override fun map(from: FirebaseCardResult): Collection<ArtEntity> {
        return from.values.mapNotNull { card ->
            if (card.isReleased) {
                card.variations.values.firstOrNull()?.let { variation ->
                    ArtEntity(
                        variation.variationId,
                        card.ingameId,
                        variation.art.original,
                        variation.art.high,
                        variation.art.medium,
                        variation.art.low,
                        variation.art.thumbnail
                    )
                }
            } else {
                null
            }
        }
    }
}