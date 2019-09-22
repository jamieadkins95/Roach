package com.jamieadkins.gwent.domain.latest

data class DeckOfTheDay(
    val name: String,
    val id: String,
    val url: String,
    val author: String,
    val votes: Int
)