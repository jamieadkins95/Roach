package com.jamieadkins.gwent.data.update.repository

import com.jamieadkins.gwent.data.update.model.FirebasePatchResult
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.jamieadkins.gwent.data.*

class PatchRepository(private val storeManager: StoreManager) {

    val gson = Gson()
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build()
            .create(CardsApi::class.java)

    fun getLatestPatchId(): Single<String> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        return storeManager.getDataOnce<FirebasePatchResult>(barcode, cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), FirebasePatchResult::class.java, 10)
            .map { it.patch }
    }
}