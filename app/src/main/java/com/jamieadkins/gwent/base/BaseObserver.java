package com.jamieadkins.gwent.base;

import android.util.Log;

import com.jamieadkins.gwent.BuildConfig;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jamiea on 26/02/17.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        if (BuildConfig.DEBUG) {
            Log.v(getClass().getSimpleName(), "onSubscribe: " + d.toString());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(getClass().getSimpleName(), "Observer Error", e);
    }
}
