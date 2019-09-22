package com.jamieadkins.gwent.data.latest

import com.google.gson.annotations.SerializedName

class DeckLibraryResponse {
    @SerializedName("name") var name: String? = null
    @SerializedName("id") var id: Int? = null
    @SerializedName("url") var url: String? = null
    @SerializedName("author") var author: String? = null
    @SerializedName("votes") var votes: Int? = null
}