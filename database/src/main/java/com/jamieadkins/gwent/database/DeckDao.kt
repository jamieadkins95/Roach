package com.jamieadkins.gwent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.database.entity.DeckWithCardsEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DeckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeck(deck: DeckEntity): Long

    @Query("UPDATE ${GwentDatabase.DECK_TABLE} SET leaderId = :leaderId WHERE id = :deckId")
    fun changeDeckLeader(deckId: String, leaderId: String?)

    @Query("UPDATE ${GwentDatabase.DECK_TABLE} SET name = :name WHERE id = :deckId")
    fun changeDeckName(deckId: String, name: String)

    @Query("UPDATE ${GwentDatabase.DECK_TABLE} SET deleted = 1 WHERE id = :deckId")
    fun deleteDeck(deckId: String)

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TABLE} WHERE deleted = 0")
    fun getDecksOnce(): Single<List<DeckWithCardsEntity>>

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TABLE} WHERE deleted = 0")
    fun getDecks(): Flowable<List<DeckWithCardsEntity>>

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TABLE} WHERE id = :deckId")
    fun getDeckOnce(deckId: String): Single<DeckWithCardsEntity>

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TABLE} WHERE id = :deckId")
    fun getDeck(deckId: String): Flowable<DeckWithCardsEntity>
}
