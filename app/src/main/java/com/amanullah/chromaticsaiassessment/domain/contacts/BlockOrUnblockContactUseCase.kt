package com.amanullah.chromaticsaiassessment.domain.contacts

import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import javax.inject.Inject

class BlockOrUnblockContactUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(contact: Contact) {
        if (contact.isBlocked) {
            repository.unblockNumber(
                BlockedContact(
                    phoneNumber = contact.phoneNumber,
                    name = contact.name,
                    photoUri = contact.photoUri,
                    isBlocked = false
                )
            )
        } else {
            repository.blockNumber(
                BlockedContact(
                    phoneNumber = contact.phoneNumber,
                    name = contact.name,
                    photoUri = contact.photoUri,
                    isBlocked = true
                )
            )
        }
    }
}