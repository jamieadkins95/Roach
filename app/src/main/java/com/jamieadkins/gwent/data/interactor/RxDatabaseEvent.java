package com.jamieadkins.gwent.data.interactor;

import android.support.annotation.NonNull;

/**
 * RxJava way of handling different events.
 */

public class RxDatabaseEvent<T> {
    private static final String KEY_LOAD_COMPLETE = "com.jamieadkins.gwent.complete";
    private static final String KEY_LOADING = "com.jamieadkins.gwent.loading";

    /**
     * Special Event that says initial loading has been completed.
     */
    protected static RxDatabaseEvent INITIAL_LOAD_COMPLETE =
            new RxDatabaseEvent(KEY_LOAD_COMPLETE, null, EventType.COMPLETE);

    private EventType eventType;
    private String key;
    private T value;

    public RxDatabaseEvent(@NonNull String key, T data, @NonNull EventType eventType) {
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
        MOVED,
        COMPLETE
    }
}
