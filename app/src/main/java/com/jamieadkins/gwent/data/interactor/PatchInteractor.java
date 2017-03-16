package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.commonutils.mvp.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Card manipulation class.
 */

public interface PatchInteractor extends BaseInteractor<BasePresenter> {

    Single<String> getLatestPatch();
}
