package com.jamieadkins.gwent;

import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CollectionInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractor;

/**
 * Created by jamiea on 28/02/17.
 */

public interface InteractorContainer {

    CardsInteractor getCardsInteractor();

    DecksInteractor getDecksInteractor();

    CollectionInteractor getCollectionInteractor();

    PatchInteractor getPatchInteractor();

}
