package com.jamieadkins.gwent.database

import androidx.room.*
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface DeckCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: DeckCardEntity): Long

    @Query("SELECT * FROM ${GwentDatabase.DECK_CARD_TABLE} WHERE deckId = :deckId AND cardId = :cardId")
    fun getCardCount(deckId: String, cardId: String): Maybe<DeckCardEntity>

    @Query("SELECT * FROM ${GwentDatabase.DECK_CARD_TABLE} WHERE deckId = :deckId")
    fun getCardCounts(deckId: String): Flowable<List<DeckCardEntity>>
}
