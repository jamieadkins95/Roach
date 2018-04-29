package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent

class NewDeckRequest(newDeckBundle: NewDeckBundle) : BaseBusEvent<NewDeckBundle>(newDeckBundle)
