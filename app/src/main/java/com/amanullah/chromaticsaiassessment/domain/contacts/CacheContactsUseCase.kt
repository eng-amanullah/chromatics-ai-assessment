package com.amanullah.chromaticsaiassessment.domain.contacts

import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import javax.inject.Inject

class CacheContactsUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(contacts: List<Contact>) {
        repository.cacheContacts(contacts)
    }
}
