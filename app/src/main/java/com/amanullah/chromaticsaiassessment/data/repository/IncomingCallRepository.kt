package com.amanullah.chromaticsaiassessment.data.repository

import com.amanullah.chromaticsaiassessment.data.local.entity.Contact

interface IncomingCallRepository {
    suspend fun getCallerByPhoneNumber(phoneNumber: String): Contact?
    suspend fun insertCaller(caller: Contact)
}
