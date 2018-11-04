package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class GwentDeckClickEvent(deckId: String) : BaseBusEvent<String>(deckId)