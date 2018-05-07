package com.jamieadkins.gwent.data.update.mapper

import com.jamieadkins.gwent.data.update.model.FirebaseCategoryResult
import com.jamieadkins.gwent.database.entity.CategoryEntity

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