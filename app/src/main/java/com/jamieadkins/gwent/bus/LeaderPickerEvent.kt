package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class LeaderPickerEvent(cardId: String) : BaseBusEvent<String>(cardId)