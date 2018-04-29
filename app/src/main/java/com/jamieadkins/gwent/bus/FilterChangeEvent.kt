package com.jamieadkins.gwent.bus

import com.jamieadkins.commonutils.bus.BaseBusEvent
import com.jamieadkins.gwent.filter.FilterableItem

class FilterChangeEvent(filterableItem: FilterableItem) : BaseBusEvent<FilterableItem>(filterableItem)