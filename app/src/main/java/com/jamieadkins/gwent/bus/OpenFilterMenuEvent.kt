package com.jamieadkins.gwent.bus

import com.jamieadkins.gwent.filter.FilterType

class OpenFilterMenuEvent(filterType: FilterType) : BaseBusEvent<FilterType>(filterType)