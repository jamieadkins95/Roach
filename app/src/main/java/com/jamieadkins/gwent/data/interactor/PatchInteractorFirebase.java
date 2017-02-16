package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

/**
 * Deals with firebase.
 */

public class PatchInteractorFirebase implements PatchInteractor {
    private static final String PATH = "card-data/latest-patch";

    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mPatchReference;

    public PatchInteractorFirebase() {
        mPatchReference = mDatabase.getReference(PATH);
        // Keep Cards data in cache at all times.
        mPatchReference.keepSynced(true);
    }

    @Override
    public Observable<String> getLatestPatch() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                        ValueEventListener patchListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emitter.onNext(dataSnapshot.getValue(String.class));
                                emitter.onComplete();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mPatchReference.addListenerForSingleValueEvent(patchListener);
                    }
                });
            }
        });
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        // Do nothing.
    }
}
