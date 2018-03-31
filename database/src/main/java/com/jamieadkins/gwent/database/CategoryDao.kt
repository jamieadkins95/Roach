package com.jamieadkins.gwent.database

import android.arch.persistence.room.*
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
    fun getCategoryForLocale(categoryId: String, locale: String): Single<CategoryEntity>

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE} WHERE categoryId = :categoryId")
    fun getCategory(categoryId: String): Single<List<CategoryEntity>>

    @Query("SELECT * FROM ${GwentDatabase.CATEGORY_TABLE}")
    fun getAllCategories(): Single<List<CategoryEntity>>
}
