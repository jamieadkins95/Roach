package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class GwentCardClickEvent(cardId: String) : BaseBusEvent<String>(cardId)