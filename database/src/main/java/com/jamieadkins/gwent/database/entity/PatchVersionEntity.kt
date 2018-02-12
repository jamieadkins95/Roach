package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.PATCH_VERSION_TABLE)
data class PatchVersionEntity(@PrimaryKey val patch: String, val name: String, val lastUpdated: Long)