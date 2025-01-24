package com.amanullah.chromaticsaiassessment.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.amanullah.chromaticsaiassessment.data.local.dao.BlockedContactsDAO
import com.amanullah.chromaticsaiassessment.data.local.dao.ContactDAO
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.ContactsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDAO,
    private val blockedContactsDAO: BlockedContactsDAO
) : ContactsRepository {

    // Insert contacts into the database
    override suspend fun cacheContacts(contacts: List<Contact>) {
        contactDao.insertContacts(contacts)
    }

    // Fetch paginated contacts
    override fun getContactsPaged() = Pager(
        config = PagingConfig(
            pageSize = 20, // Number of items per page
            enablePlaceholders = false
        ),
        pagingSourceFactory = { contactDao.getContactsPaged() }
    ).flow

    // Search contacts
    override fun searchContacts(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { contactDao.searchContacts("%$query%") }
    ).flow

    // Block a contact number
    override suspend fun blockNumber(contact: BlockedContact) {
        blockedContactsDAO.blockNumber(contact)
    }

    // Unblock a contact number
    override suspend fun unblockNumber(contact: BlockedContact) {
        blockedContactsDAO.unblockNumber(contact)
    }
}
