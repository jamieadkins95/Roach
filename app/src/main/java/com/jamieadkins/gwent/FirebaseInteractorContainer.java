package com.jamieadkins.gwent;

import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.CollectionInteractor;
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;

/**
 * Created by jamiea on 28/02/17.
 */

public class FirebaseInteractorContainer implements InteractorContainer {
    @Override
    public CardsInteractor getCardsInteractor() {
        return CardsInteractorFirebase.getInstance();
    }

    @Override
    public DecksInteractor getDecksInteractor() {
        return new DecksInteractorFirebase();
    }

    @Override
    public CollectionInteractor getCollectionInteractor() {
        return new CollectionInteractorFirebase();
    }

    @Override
    public PatchInteractor getPatchInteractor() {
        return new PatchInteractorFirebase();
    }
}
