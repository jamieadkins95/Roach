package com.jamieadkins.gwent;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jamieadkins.jgaw.Card;
import com.jamieadkins.jgaw.CardListResult;
import com.jamieadkins.jgaw.GwentApiV0;

import org.junit.BeforeClass;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GwentApiTest {
    private static GwentApiV0 gwentApi;

    @BeforeClass
    public static void setupGwentApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gwentapi.com/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        gwentApi = retrofit.create(GwentApiV0.class);
    }

    @Test
    public void getCardTest() throws Exception {
        TestObserver<Card> testObserver = new TestObserver<>();
        gwentApi.getCard("triss-mistress-of-magic")
                .subscribe(testObserver);
        testObserver.assertNoErrors();
    }

    @Test
    public void getCardList() throws Exception {
        TestObserver<CardListResult> testObserver = new TestObserver<>();
        gwentApi.listCards()
                .subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
    }
}