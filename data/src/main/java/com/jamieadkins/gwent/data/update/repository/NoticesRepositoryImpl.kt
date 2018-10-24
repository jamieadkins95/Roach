package com.jamieadkins.gwent.data.update.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jamieadkins.gwent.data.BuildConfig
import com.jamieadkins.gwent.data.CardsApi
import com.jamieadkins.gwent.data.Constants
import com.jamieadkins.gwent.data.StoreManager
import com.jamieadkins.gwent.data.update.model.FirebaseNotice
import com.jamieadkins.gwent.domain.update.model.Notice
import com.jamieadkins.gwent.domain.update.repository.NoticesRepository
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NoticesRepositoryImpl @Inject constructor(private val storeManager: StoreManager) : NoticesRepository {

    val gson = Gson()
    private val cardsApi = Retrofit.Builder()
        .baseUrl(Constants.CARDS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .validateEagerly(BuildConfig.DEBUG)
        .build()
        .create(CardsApi::class.java)

    override fun getNotices(): Observable<List<Notice>> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("notices"))
        return storeManager.getDataOnce<List<FirebaseNotice>>(barcode, cardsApi.fetchNotices("en"), genericType<List<FirebaseNotice>>(), 10)
            .map {
                it.map { notice ->
                    Notice(
                        notice.id,
                        notice.title,
                        notice.body,
                        notice.enabled
                    )
                }
            }
            .toObservable()
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
}