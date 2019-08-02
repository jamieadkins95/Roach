package com.jamieadkins.gwent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.domain.GwentFaction
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

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + " WHERE faction=:faction AND type='Leader'")
    fun getLeaders(faction: String): Flowable<List<CardWithArtEntity>>

    @Transaction
    @Query("SELECT * FROM " + GwentDatabase.CARD_TABLE + " WHERE faction IN(:factions) OR secondaryFaction IN(:factions)")
    fun getCardsInFactions(factions: List<String>): Flowable<List<CardWithArtEntity>>

    @Query("SELECT COUNT(*) FROM " + GwentDatabase.CARD_TABLE)
    fun count(): Flowable<Int>
}
