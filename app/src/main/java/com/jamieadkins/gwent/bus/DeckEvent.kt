package com.jamieadkins.gwent.bus

import com.jamieadkins.gwent.data.CardDetails

class DeckEvent(bundle: DeckEventBundle) : BaseBusEvent<DeckEvent.DeckEventBundle>(bundle) {
    enum class Event {
        ADD_CARD,
        REMOVE_CARD
    }

    class DeckEventBundle(val event: Event, val card: CardDetails)
}