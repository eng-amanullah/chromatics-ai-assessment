package com.amanullah.chromaticsaiassessment.data.repositoryimpl

import com.amanullah.chromaticsaiassessment.data.local.dao.IncomingCallDAO
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.IncomingCallRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomingCallRepositoryImpl @Inject constructor(
    private val incomingCallDAO: IncomingCallDAO?
) : IncomingCallRepository {

    private val hardCodeCallersList = listOf(
        Contact(phoneNumber = "01710517010", name = "Amanullah Sarker"),
        Contact(phoneNumber = "1234567890", name = "Person 1"),
        Contact(phoneNumber = "2345678901", name = "Person 2"),
        Contact(phoneNumber = "3456789012", name = "Person 3"),
        Contact(phoneNumber = "4567890123", name = "Person 4"),
        Contact(phoneNumber = "5678901234", name = "Person 5"),
        Contact(phoneNumber = "6789012345", name = "Person 6"),
        Contact(phoneNumber = "7890123456", name = "Person 7"),
        Contact(phoneNumber = "8901234567", name = "Person 8"),
        Contact(phoneNumber = "9012345678", name = "Person 9"),
        Contact(phoneNumber = "0123456789", name = "Person 10"),
    )

    override suspend fun getCallerByPhoneNumber(phoneNumber: String): Contact? {
        val databaseResult = incomingCallDAO?.getCallerByPhoneNumber(phoneNumber)
        val hardCodedResult = hardCodeCallersList.firstOrNull { it.phoneNumber == phoneNumber }
        return databaseResult ?: hardCodedResult
    }

    override suspend fun insertCaller(caller: Contact) {
        incomingCallDAO?.insertCaller(caller)
    }
}
