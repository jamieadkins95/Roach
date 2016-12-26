package com.jamieadkins.commonutils.mvp;

public interface BasePresenter<T> {
    void start();

    void bindView(T view);

    void unbindView();
}
