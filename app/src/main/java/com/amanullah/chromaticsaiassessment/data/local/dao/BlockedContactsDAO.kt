package com.amanullah.chromaticsaiassessment.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact

@Dao
interface BlockedContactsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun blockNumber(contact: BlockedContact)

    @Delete
    suspend fun unblockNumber(contact: BlockedContact)

    @Query("SELECT * FROM blocked_contacts")
    suspend fun getAllBlockedNumbers(): List<BlockedContact>

    @Query("SELECT * FROM blocked_contacts WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%'")
    suspend fun searchBlockedNumbers(query: String): List<BlockedContact>
}