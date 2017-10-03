package com.jamieadkins.gwent.data

class SearchResult(val hits: List<String>?, val nbHits: Long, val query: String?) {
    constructor() : this(null, 0, null)

    override fun toString(): String {
        return query + ":" + nbHits
    }
}