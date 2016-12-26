package com.jamieadkins.gwent.main;

import android.support.annotation.NonNull;

import com.jamieadkins.commonutils.mvp.BasePresenter;

/**
 * Created by jamiea on 26/12/16.
 */

public interface PresenterFactory<T extends BasePresenter> {

    /**
     * Create a new instance of a Presenter
     *
     * @return The Presenter instance
     */
    @NonNull
    T createPresenter();
}
