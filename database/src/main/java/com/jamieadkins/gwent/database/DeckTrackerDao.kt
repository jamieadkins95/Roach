package com.jamieadkins.gwent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.database.entity.DeckTrackerCardEntity
import com.jamieadkins.gwent.database.entity.DeckTrackerGameEntity
import com.jamieadkins.gwent.database.entity.DeckTrackerGameWithCardsEntity
import com.jamieadkins.gwent.database.entity.DeckWithCardsEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DeckTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: DeckTrackerGameEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCardPlayed(entity: DeckTrackerCardEntity): Long

    @Query("UPDATE ${GwentDatabase.DECK_TRACKER_GAME_TABLE} SET deleted = 1 WHERE id = :deckId")
    fun deleteGame(gameId: String)

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TRACKER_GAME_TABLE} WHERE deleted = 0")
    fun getGames(): Flowable<List<DeckTrackerGameWithCardsEntity>>

    @Transaction
    @Query("SELECT * FROM ${GwentDatabase.DECK_TRACKER_GAME_TABLE} WHERE id = :deckId")
    fun getGame(gameId: String): Flowable<DeckTrackerGameWithCardsEntity>
}
