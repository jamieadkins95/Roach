package com.jamieadkins.gwent.data

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Base URL: https://gwent-9e62a.firebaseio.com/
 */
interface CardsApi {

    @GET("card-data/{patch}.json")
    fun fetchCards(@Path("patch") patch: String): Single<ResponseBody>

    @GET("patch/{version}.json")
    fun fetchPatch(@Path("version") version: String): Single<ResponseBody>
}