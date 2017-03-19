package com.jamieadkins.gwent.base;

import android.util.Log;

import com.jamieadkins.gwent.BuildConfig;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jamiea on 19/03/17.
 */

public abstract class BaseCompletableObserver implements CompletableObserver {
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
