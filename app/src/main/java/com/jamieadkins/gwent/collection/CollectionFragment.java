package com.jamieadkins.gwent.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.data.Collection;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CollectionFragment extends BaseCardListFragment implements CollectionContract.View {
    CollectionContract.Presenter mPresenter;
    CollectionRecyclerViewAdapter mAdapter;

    public CollectionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CollectionRecyclerViewAdapter(
                new CollectionCardViewHolder.CollectionButtonListener() {
                    @Override
                    public void addCard(String cardId) {
                        mPresenter.addCard(cardId);
                    }

                    @Override
                    public void removeCard(String cardId) {
                        mPresenter.removeCard(cardId);
                    }
                });
        setRecyclerViewAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collection;
    }

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
                        mAdapter.setCollection(value);
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
    public void setPresenter(CollectionContract.Presenter presenter) {
        setCardsPresenter(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }
}
