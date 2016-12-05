package com.jamieadkins.jgaw;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jamieadkins.jgaw.Card.Card;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Public facing api client wrapper.
 */

public class GwentApiClient {
    private static final String API_BASE_URL = "https://api.gwentapi.com/v0/";

    private final GwentApiV0 mGwentApi;

    private CardResultListener mCardResultListener;

    public GwentApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mGwentApi = retrofit.create(GwentApiV0.class);
    }

    public void setCardListener(CardResultListener cardResultListener) {
        mCardResultListener = cardResultListener;
    }

    public void retrieveCard(String id) {
        mGwentApi.getCard(id)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Card>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Card value) {
                        if (mCardResultListener != null) {
                            mCardResultListener.onCardRetrieved(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mCardResultListener != null) {
                            mCardResultListener.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static String getIdFromApiUrl(String apiUrl) {
        final int lastSlash = apiUrl.lastIndexOf("/");
        return apiUrl.substring(lastSlash);
    }
}
