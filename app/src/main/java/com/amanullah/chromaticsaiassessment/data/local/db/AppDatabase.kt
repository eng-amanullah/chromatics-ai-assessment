package com.amanullah.chromaticsaiassessment.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amanullah.chromaticsaiassessment.data.local.dao.BlockedContactsDAO
import com.amanullah.chromaticsaiassessment.data.local.dao.IncomingCallDAO
import com.amanullah.chromaticsaiassessment.data.local.dao.ContactDAO
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact

@Database(entities = [Contact::class, BlockedContact::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDAO
    abstract fun identifiedCallerDAO(): IncomingCallDAO
    abstract fun blockedContactsDAO(): BlockedContactsDAO
}
