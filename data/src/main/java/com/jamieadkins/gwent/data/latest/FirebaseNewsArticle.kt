package com.jamieadkins.gwent.data.latest

import com.google.gson.annotations.SerializedName

class FirebaseNewsArticle {
    @SerializedName("id") var id: String? = null
    @SerializedName("title") var title: String? = null
    @SerializedName("url") var url: String? = null
    @SerializedName("image") var image: String? = null
}