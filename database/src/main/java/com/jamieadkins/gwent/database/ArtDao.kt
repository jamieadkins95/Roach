package com.jamieadkins.gwent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ArtDao {

    @Query("SELECT * FROM " + GwentDatabase.ART_TABLE + " WHERE cardId=:cardId")
    fun getCardArt(cardId: String): Single<List<ArtEntity>>

    @Query("SELECT * FROM " + GwentDatabase.ART_TABLE + "  WHERE cardId IN(:cardIds)")
    fun getCardArt(cardIds: List<String>): Single<List<ArtEntity>>

    @Query("SELECT * FROM " + GwentDatabase.ART_TABLE)
    fun getCardArt(): Single<List<ArtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArt(items: Collection<ArtEntity>)

}
