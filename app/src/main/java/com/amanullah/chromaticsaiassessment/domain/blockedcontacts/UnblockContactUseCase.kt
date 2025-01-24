package com.amanullah.chromaticsaiassessment.domain.blockedcontacts

import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.repository.BlockedContactsRepository
import javax.inject.Inject

class UnblockContactUseCase @Inject constructor(
    private val repository: BlockedContactsRepository
) {
    suspend operator fun invoke(contact: BlockedContact) {
        repository.unblockNumber(contact)
    }
}
