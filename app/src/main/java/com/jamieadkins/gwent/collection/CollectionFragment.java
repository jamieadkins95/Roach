package com.jamieadkins.gwent.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.jamieadkins.commonutils.mvp.PresenterFactory;
import com.jamieadkins.gwent.InteractorContainer;
import com.jamieadkins.gwent.InteractorContainers;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.data.Collection;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CollectionFragment extends BaseCardListFragment implements CollectionContract.View,
        PresenterFactory<CollectionContract.Presenter> {
    CollectionContract.Presenter mPresenter;

    public CollectionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.my_collection));
        mPresenter = createPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);

        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.new_keg);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open keg.
            }
        });
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mPresenter.getCollection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Collection>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Collection value) {
                        getRecyclerViewAdapter().setCardCollection(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(active);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    @Override
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.COLLECTION)
                .withCollectionButtonListener(new CollectionCardViewHolder.CollectionButtonListener() {
                    @Override
                    public void addCard(String cardId, String variationId) {
                        mPresenter.addCard(cardId, variationId);
                    }

                    @Override
                    public void removeCard(String cardId, String variationId) {
                        mPresenter.removeCard(cardId, variationId);
                    }
                })
                .build();
    }

    @Override
    public CollectionContract.Presenter createPresenter() {
        InteractorContainer interactorContainer = InteractorContainers.getFromApp(getActivity());
        CollectionContract.Presenter presenter = new CollectionPresenter(
                this,
                interactorContainer.getCollectionInteractor(),
                interactorContainer.getCardsInteractor());
        setCardsPresenter(presenter);
        return presenter;
    }
}
