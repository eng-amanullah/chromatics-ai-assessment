package com.amanullah.chromaticsaiassessment.data.repository

import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact

interface BlockedContactsRepository {
    suspend fun unblockNumber(contact: BlockedContact)
    suspend fun getAllBlockedNumbers(): List<BlockedContact>
    suspend fun searchBlockedNumbers(query: String): List<BlockedContact>
}
