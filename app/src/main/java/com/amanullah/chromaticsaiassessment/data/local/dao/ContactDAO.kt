package com.amanullah.chromaticsaiassessment.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact

@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContacts(contacts: List<Contact>)

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getContactsPaged(): PagingSource<Int, Contact>

    @Query("SELECT * FROM contacts WHERE name LIKE :searchQuery OR phoneNumber LIKE :searchQuery ORDER BY name ASC")
    fun searchContacts(searchQuery: String): PagingSource<Int, Contact>

    @Query("DELETE FROM contacts")
    suspend fun clearContacts()
}