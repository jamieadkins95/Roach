package com.jamieadkins.gwent.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(items: Collection<CardEntity>)

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE)
    fun getCardsOnce(): Single<List<CardWithArtEntity>>

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE)
    fun getCards(): Flowable<List<CardWithArtEntity>>

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + " WHERE id=:cardId")
    fun getCard(cardId: String): Flowable<CardWithArtEntity>

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + "  WHERE id IN(:ids)")
    fun getCards(ids: List<String>): Flowable<List<CardWithArtEntity>>

    @Query("SELECT COUNT(*) FROM " + GwentDatabase.CARD_TABLE)
    fun count(): Flowable<Int>
}
