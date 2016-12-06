package com.jamieadkins.jgaw;

import com.jamieadkins.jgaw.card.CardStubResult;

import java.util.List;

/**
 * Object representing a Gwent card.
 */

public class CardListResult {
    private String count;
    private List<CardStubResult> results;

    public List<CardStubResult> getResults() {
        return results;
    }
}
