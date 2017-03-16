package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.data.FirebaseUtils;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;

/**
 * Deals with firebase.
 */

public class PatchInteractorFirebase implements PatchInteractor {
    private static final String PATH = "card-data/latest-patch";

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mPatchReference;

    public PatchInteractorFirebase() {
        mPatchReference = mDatabase.getReference(PATH);
        // Keep Cards data in cache at all times.
        mPatchReference.keepSynced(true);
    }

    @Override
    public Single<String> getLatestPatch() {
        return Single.defer(new Callable<SingleSource<? extends String>>() {
            @Override
            public SingleSource<? extends String> call() throws Exception {
                return Single.create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(final SingleEmitter<String> emitter) throws Exception {
                        ValueEventListener patchListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emitter.onSuccess(dataSnapshot.getValue(String.class));
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
}
