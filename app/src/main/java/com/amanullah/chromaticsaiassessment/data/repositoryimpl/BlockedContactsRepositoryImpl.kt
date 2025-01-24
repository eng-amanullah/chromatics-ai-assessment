package com.amanullah.chromaticsaiassessment.data.repositoryimpl

import com.amanullah.chromaticsaiassessment.data.local.dao.BlockedContactsDAO
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.repository.BlockedContactsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedContactsRepositoryImpl @Inject constructor(
    private val blockedContactsDAO: BlockedContactsDAO
) : BlockedContactsRepository {
    // Unblock a contact number
    override suspend fun unblockNumber(contact: BlockedContact) {
        blockedContactsDAO.unblockNumber(contact = contact)
    }

    // Get all blocked contacts
    override suspend fun getAllBlockedNumbers(): List<BlockedContact> {
        return blockedContactsDAO.getAllBlockedNumbers()
    }

    override suspend fun searchBlockedNumbers(query: String): List<BlockedContact> {
        return blockedContactsDAO.searchBlockedNumbers(query = query)
    }
}
