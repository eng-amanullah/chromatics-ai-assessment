package com.amanullah.chromaticsaiassessment.data.repository

import androidx.paging.PagingData
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun cacheContacts(contacts: List<Contact>)
    fun getContactsPaged(): Flow<PagingData<Contact>>
    fun searchContacts(query: String): Flow<PagingData<Contact>>
    suspend fun blockNumber(contact: BlockedContact)
    suspend fun unblockNumber(contact: BlockedContact)
}
