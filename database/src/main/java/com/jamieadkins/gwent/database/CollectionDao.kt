package com.jamieadkins.gwent.database

import android.arch.persistence.room.*
import com.jamieadkins.gwent.database.entity.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CollectionEntity): Long

    @Query("UPDATE ${GwentDatabase.COLLECTION_TABLE} SET count = :count WHERE cardId = :cardId AND variationId = :variationId")
    fun updateCardCount(cardId: String, variationId: String, count: Int)

    @Query("SELECT * FROM ${GwentDatabase.COLLECTION_TABLE} WHERE cardId = :cardId AND variationId = :variationId")
    fun getCollectionCardCount(cardId: String, variationId: String): Maybe<CollectionEntity>

    @Query("SELECT * FROM ${GwentDatabase.COLLECTION_TABLE}")
    fun getCollection(): Flowable<List<CollectionEntity>>
}
