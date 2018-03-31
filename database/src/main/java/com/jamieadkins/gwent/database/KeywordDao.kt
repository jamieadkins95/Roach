package com.jamieadkins.gwent.database

import android.arch.persistence.room.*
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
    fun getKeywordForLocale(keywordId: String, locale: String): Single<KeywordEntity>

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE} WHERE keywordId = :keywordId")
    fun getKeyword(keywordId: String): Single<List<KeywordEntity>>

    @Query("SELECT * FROM ${GwentDatabase.KEYWORD_TABLE}")
    fun getAllKeywords(): Single<List<KeywordEntity>>
}
