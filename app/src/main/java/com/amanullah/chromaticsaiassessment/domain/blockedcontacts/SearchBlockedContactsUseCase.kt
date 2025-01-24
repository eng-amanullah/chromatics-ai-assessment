package com.amanullah.chromaticsaiassessment.domain.blockedcontacts

import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.repository.BlockedContactsRepository
import javax.inject.Inject

class SearchBlockedContactsUseCase @Inject constructor(
    private val repository: BlockedContactsRepository
) {
    suspend operator fun invoke(query: String): List<BlockedContact> {
        return if (query.isNotBlank()) {
            repository.searchBlockedNumbers(query)
        } else {
            emptyList()
        }
    }
}
