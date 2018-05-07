package com.jamieadkins.gwent.data.update.mapper

import com.jamieadkins.gwent.data.update.model.FirebaseKeywordResult
import com.jamieadkins.gwent.database.entity.KeywordEntity

object GwentKeywordMapper {

    fun mapToKeywordEntityList(result: FirebaseKeywordResult): Collection<KeywordEntity> {
        val keywords = mutableListOf<KeywordEntity>()
        result.keys.forEach { id ->
            result[id]?.entries?.forEach {
                keywords.add(
                        KeywordEntity(
                                id,
                                it.key,
                                id,
                                it.value.text ?: "")
                )
            }
        }
        return keywords
    }
}