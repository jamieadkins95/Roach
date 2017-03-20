package com.jamieadkins.gwent.base;

import android.util.Log;

import com.jamieadkins.gwent.BuildConfig;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jamiea on 26/02/17.
 */

public abstract class BaseSingleObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {
        // Do nothing.
    }

    @Override
    public void onError(Throwable e) {
        Log.e(getClass().getSimpleName(), "Observer Error", e);
    }
}
