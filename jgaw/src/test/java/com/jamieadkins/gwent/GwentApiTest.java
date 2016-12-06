package com.jamieadkins.gwent;

import com.jamieadkins.jgaw.card.Card;
import com.jamieadkins.jgaw.CardListResult;
import com.jamieadkins.jgaw.GwentApiV0;

import org.junit.BeforeClass;
import org.junit.Test;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertTrue;

/**
 * GwentApi tests.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GwentApiTest {
    private static GwentApiV0 mGwentApi;

    @BeforeClass
    public static void setupGwentApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gwentapi.com/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGwentApi = retrofit.create(GwentApiV0.class);
    }

    @Test
    public void getCardTest() throws Exception {
        Response<Card> response = mGwentApi.getCard("triss-mistress-of-magic").execute();
        assertTrue(response.isSuccessful());
    }

    @Test
    public void getCardList() throws Exception {
        Response<CardListResult> response = mGwentApi.getAllCards().execute();
        assertTrue(response.isSuccessful());
    }
}