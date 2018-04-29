package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class CollectionEvent(bundle: CollectionEventBundle) : BaseBusEvent<CollectionEvent.CollectionEventBundle>(bundle) {
    enum class Event {
        ADD_CARD,
        REMOVE_CARD
    }

    class CollectionEventBundle(val event: Event, val cardId: String)
}