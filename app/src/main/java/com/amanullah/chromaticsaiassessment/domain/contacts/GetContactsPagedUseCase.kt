package com.amanullah.chromaticsaiassessment.domain.contacts

import androidx.paging.PagingData
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsPagedUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    operator fun invoke(): Flow<PagingData<Contact>> {
        return repository.getContactsPaged()
    }
}
