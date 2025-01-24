package com.amanullah.chromaticsaiassessment.domain.contacts

import androidx.paging.PagingData
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchContactsUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Contact>> {
        return if (query.isEmpty()) {
            flowOf(PagingData.empty())
        } else {
            repository.searchContacts(query)
        }
    }
}