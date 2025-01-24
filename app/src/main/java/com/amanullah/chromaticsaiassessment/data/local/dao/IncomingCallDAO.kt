package com.amanullah.chromaticsaiassessment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact

@Dao
interface IncomingCallDAO {
    @Query("SELECT * FROM contacts WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getCallerByPhoneNumber(phoneNumber: String): Contact?

    @Insert
    suspend fun insertCaller(caller: Contact)
}
