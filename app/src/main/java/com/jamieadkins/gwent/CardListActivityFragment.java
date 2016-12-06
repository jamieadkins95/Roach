package com.jamieadkins.gwent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.jgaw.CardListResult;
import com.jamieadkins.jgaw.GwentApiClient;
import com.jamieadkins.jgaw.card.BaseApiResult;
import com.jamieadkins.jgaw.card.Card;
import com.jamieadkins.jgaw.card.CardStubResult;
import com.jamieadkins.jgaw.exception.GwentApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardListActivityFragment extends Fragment {
    private RecyclerView mCardListView;
    private CardRecyclerViewAdapter mAdapter;

    public CardListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);

        mCardListView = (RecyclerView) rootView.findViewById(R.id.results);
        setupRecyclerView(mCardListView);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        final GwentApiClient gwentApiClient = new GwentApiClient();
        Observable.defer(new Callable<ObservableSource<? extends List<CardStubResult>>>() {
            @Override
            public ObservableSource<? extends List<CardStubResult>> call() throws Exception {
                return Observable.just(gwentApiClient.getAllCards());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CardStubResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CardStubResult> value) {
                        ArrayList<BaseApiResult> cards = new ArrayList<>();
                        cards.addAll(value);
                        mAdapter = new CardRecyclerViewAdapter(cards);
                        mCardListView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
