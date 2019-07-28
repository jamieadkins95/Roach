package com.jamieadkins.decktracker.data

import com.google.gson.Gson
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictorRepository
import dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
abstract class CardPredictorDataModule {

    @Binds
    @DeckTrackerScope
    abstract fun cardPredictorRepository(repository: CardPredictorRepositoryImpl): CardPredictorRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesCardPredictorRetrofit(okHttpClient: Lazy<OkHttpClient>, gson: Gson): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://us-central1-gwent-9e62a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callFactory { okHttpClient.get().newCall(it) }
                .build()
        }

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesCardPredictorService(retrofit: Retrofit): CardPredictorApi = retrofit.create(CardPredictorApi::class.java)

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesOkHttpClient(): OkHttpClient {
            val okHttpBuilder = OkHttpClient.Builder()

            return okHttpBuilder
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
        }

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesGson(): Gson = Gson()
    }
}