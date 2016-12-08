package com.jamieadkins.commonutils.mvp;

public interface BaseInteractor<T> {
    void setPresenter(T presenter);

    void requestData();

    void stopData();
}
