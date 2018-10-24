package com.jamieadkins.gwent.domain.update.model

data class Notice(val id: Long, val title: String, val body: String = "", val enabled: Boolean)