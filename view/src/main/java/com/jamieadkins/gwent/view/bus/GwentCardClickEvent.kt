package com.jamieadkins.gwent.view.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class GwentCardClickEvent(cardId: String) : BaseBusEvent<String>(cardId)