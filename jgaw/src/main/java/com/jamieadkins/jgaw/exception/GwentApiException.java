package com.jamieadkins.jgaw.exception;


/**
 * Base exception for GwentApi actions.
 */

public class GwentApiException extends Exception {
    public GwentApiException(String message) {
        super(message);
    }
}
