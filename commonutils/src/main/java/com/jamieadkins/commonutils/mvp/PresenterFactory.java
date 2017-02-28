package com.jamieadkins.commonutils.mvp;

/**
 * Created by jamiea on 28/02/17.
 */

public interface PresenterFactory<T extends BasePresenter> {
    T createPresenter();
}
