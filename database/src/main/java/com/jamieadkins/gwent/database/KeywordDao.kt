package com.jamieadkins.gwent.database

import androidx.room.*
import com.jamieadkins.gwent.database.entity.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface KeywordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: KeywordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Collection<KeywordEntity>)

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE} WHERE keywordId = :keywordId AND locale = :locale")
    fun getKeywordForLocale(keywordId: String, locale: String): Flowable<KeywordEntity>

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE} WHERE locale = :locale")
    fun getKeywordsForLocale(locale: String): Flowable<List<KeywordEntity>>

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE} WHERE keywordId = :keywordId")
    fun getKeyword(keywordId: String): Flowable<List<KeywordEntity>>

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE}")
    fun getAllKeywords(): Flowable<List<KeywordEntity>>
}
