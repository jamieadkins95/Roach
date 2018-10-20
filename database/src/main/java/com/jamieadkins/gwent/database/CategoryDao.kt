package com.jamieadkins.gwent.database

import androidx.room.*
import com.jamieadkins.gwent.database.entity.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Collection<CategoryEntity>)

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE} WHERE categoryId = :categoryId AND locale = :locale")
    fun getCategoryForLocale(categoryId: String, locale: String): Flowable<CategoryEntity>

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE} WHERE locale = :locale")
    fun getCategoriesForLocale(locale: String): Flowable<List<CategoryEntity>>

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE} WHERE categoryId = :categoryId")
    fun getCategory(categoryId: String): Flowable<List<CategoryEntity>>

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE}")
    fun getAllCategories(): Flowable<List<CategoryEntity>>
}
