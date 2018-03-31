package com.jamieadkins.gwent.data.repository.update

import com.jamieadkins.gwent.data.keyword.FirebaseCategoryResult
import com.jamieadkins.gwent.data.keyword.FirebaseKeywordResult
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity

object GwentCategoryMapper {

    fun mapToCategoryEntityList(result: FirebaseCategoryResult): Collection<CategoryEntity> {
        val categories = mutableListOf<CategoryEntity>()
        result.keys.forEach { id ->
            result[id]?.entries?.forEach {
                categories.add(
                        CategoryEntity(
                                id,
                                it.key,
                                it.value)
                )
            }
        }
        return categories
    }
}