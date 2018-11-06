package com.jamieadkins.gwent.domain.filter

import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import javax.inject.Inject

class ResetFilterUseCase @Inject constructor(private val filterRepository: FilterRepository) {

    fun resetFilter(deckId: String = "") = filterRepository.resetFilter(deckId)
}