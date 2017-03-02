package com.jamieadkins.gwent;

import android.support.test.runner.AndroidJUnit4;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class CardsInteractorTest {
    private static final String VALID_CARD_ID = "122212"; // Nenneke.
    private static final int CARD_COUNT = 300;
    private CardsInteractor cardsInteractor;
    private CardFilter cardFilter;

    @Before
    public void setUp() {
        cardsInteractor = CardsInteractorFirebase.getInstance();
    }

    @Test
    public void testGetCard() throws Exception {
        TestObserver<RxDatabaseEvent<CardDetails>> observer = TestObserver.create();
        cardsInteractor.getCard(VALID_CARD_ID).subscribe(observer);

        // Wait until complete.
        observer.awaitTerminalEvent();

        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        CardDetails result = ((RxDatabaseEvent<CardDetails>) observer.getEvents().get(0).get(0)).getValue();
        assertTrue(result.getIngameId().equals(VALID_CARD_ID));
    }

    @Test
    public void testGetCardsNoFilter() throws Exception {
        TestObserver<RxDatabaseEvent<CardDetails>> observer = TestObserver.create();
        cardFilter = new CardFilter();
        cardsInteractor.getCards(cardFilter).subscribe(observer);

        // Wait until complete.
        observer.awaitTerminalEvent();
        observer.assertComplete();
        observer.assertNoErrors();

        assertTrue(observer.getEvents().get(0).size() > CARD_COUNT);
    }

    @Test
    public void testGetCardsFactionFilter() throws Exception {
        TestObserver<RxDatabaseEvent<CardDetails>> observer = TestObserver.create();
        cardFilter = new CardFilter();
        cardFilter.put(Faction.MONSTERS_ID, false);
        cardFilter.put(Faction.NILFGAARD_ID, false);
        cardFilter.put(Faction.SKELLIGE_ID, false);
        cardFilter.put(Faction.SCOIATAEL_ID, false);
        cardFilter.put(Faction.NEUTRAL_ID, false);
        cardFilter.put(Faction.NORTHERN_REALMS_ID, true);
        cardsInteractor.getCards(cardFilter).subscribe(observer);

        // Wait until complete.
        observer.awaitTerminalEvent();
        observer.assertComplete();
        observer.assertNoErrors();

        for (Object object : observer.getEvents().get(0)) {
            RxDatabaseEvent<CardDetails> event = (RxDatabaseEvent<CardDetails>) object;
            CardDetails details = event.getValue();
            assertTrue(details.getFaction().equals(Faction.NORTHERN_REALMS));
        }
    }

    @Test
    public void testGetCardsTypeFilter() throws Exception {
        TestObserver<RxDatabaseEvent<CardDetails>> observer = TestObserver.create();
        cardFilter = new CardFilter();
        cardFilter.put(Type.LEADER_ID, true);
        cardFilter.put(Type.GOLD_ID, false);
        cardFilter.put(Type.SILVER_ID, false);
        cardFilter.put(Type.BRONZE_ID, false);
        cardsInteractor.getCards(cardFilter).subscribe(observer);

        // Wait until complete.
        observer.awaitTerminalEvent();
        observer.assertComplete();
        observer.assertNoErrors();

        for (Object object : observer.getEvents().get(0)) {
            RxDatabaseEvent<CardDetails> event = (RxDatabaseEvent<CardDetails>) object;
            CardDetails details = event.getValue();
            assertTrue(details.getType().equals(Type.LEADER_ID));
        }
    }

    @Test
    public void testGetCardsRarityFilter() throws Exception {
        TestObserver<RxDatabaseEvent<CardDetails>> observer = TestObserver.create();
        cardFilter = new CardFilter();
        cardFilter.put(Rarity.LEGENDARY_ID, true);
        cardFilter.put(Rarity.EPIC_ID, false);
        cardFilter.put(Rarity.RARE_ID, false);
        cardFilter.put(Rarity.COMMON_ID, false);
        cardsInteractor.getCards(cardFilter).subscribe(observer);

        // Wait until complete.
        observer.awaitTerminalEvent();
        observer.assertComplete();
        observer.assertNoErrors();

        for (Object object : observer.getEvents().get(0)) {
            RxDatabaseEvent<CardDetails> event = (RxDatabaseEvent<CardDetails>) object;
            CardDetails details = event.getValue();
            assertTrue(details.getRarity().equals(Rarity.LEGENDARY_ID));
        }
    }
}