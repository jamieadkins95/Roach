package com.jamieadkins.gwent.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(items: Collection<CardEntity>)

    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE)
    fun getCards(): Single<List<CardEntity>>

    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE)
    fun subscribeToCards(): Flowable<List<CardEntity>>

    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + " WHERE id=:cardId")
    fun getCard(cardId: String): Single<CardEntity>

    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + "  WHERE id IN(:ids)")
    fun getCards(ids: List<String>): Single<List<CardEntity>>

    @Query("SELECT * FROM " + GwentDatabase.ART_TABLE + " WHERE cardId=:cardId")
    fun getCardArt(cardId: String): Single<List<ArtEntity>>

    @Query("SELECT * FROM " + GwentDatabase.ART_TABLE)
    fun getCardArt(): Single<List<ArtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArt(items: Collection<ArtEntity>)

}
