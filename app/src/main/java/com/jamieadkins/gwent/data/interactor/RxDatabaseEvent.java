package com.jamieadkins.gwent.data.interactor;

import android.support.annotation.NonNull;

/**
 * RxJava way of handling different events.
 */

public class RxDatabaseEvent<T> {
    private EventType eventType;
    private String key;
    private T value;

    public RxDatabaseEvent(@NonNull String key, @NonNull T data, @NonNull EventType eventType) {
        this.key = key;
        this.value = data;
        this.eventType = eventType;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public T getValue() {
        return value;
    }

    @NonNull
    public EventType getEventType() {
        return eventType;
    }

    public enum EventType {
        ADDED,
        CHANGED,
        REMOVED,
        MOVED
    }
}
