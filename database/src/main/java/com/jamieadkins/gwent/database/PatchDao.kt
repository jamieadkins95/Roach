package com.jamieadkins.gwent.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface PatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatchVersion(patch: PatchVersionEntity)

    @Query("SELECT * FROM " + GwentDatabase.PATCH_VERSION_TABLE + " WHERE patch=:patch")
    fun getPatchVersion(patch: String): PatchVersionEntity?

}
