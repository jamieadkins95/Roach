package com.jamieadkins.gwent.data.interactor

import com.jamieadkins.gwent.data.CardDetails

object CardCache {
    var cardsById: MutableMap<String, CardDetails> = mutableMapOf()

    fun clear() {
        cardsById = mutableMapOf()
    }
}