package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.domain.card.model.GwentCardArt
import javax.inject.Inject

class GwentCardArtMapper @Inject constructor() : Mapper<ArtEntity, GwentCardArt>() {

    override fun map(from: ArtEntity): GwentCardArt {
        return GwentCardArt(
            from.original,
            from.high,
            from.medium,
            from.low,
            from.thumbnail
        )
    }
}