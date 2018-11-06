package com.jamieadkins.gwent.domain.filter

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import javax.inject.Inject

class SetFilterUseCase @Inject constructor(private val filterRepository: FilterRepository) {

    fun setFilter(deckId: String = "", cardFilter: CardFilter) = filterRepository.setFilter(deckId, cardFilter)
}