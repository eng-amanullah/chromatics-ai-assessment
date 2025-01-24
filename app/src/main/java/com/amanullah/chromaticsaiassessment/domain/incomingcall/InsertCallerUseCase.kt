package com.amanullah.chromaticsaiassessment.domain.incomingcall

import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.IncomingCallRepository
import javax.inject.Inject

class InsertCallerUseCase @Inject constructor(
    private val repository: IncomingCallRepository
) {
    suspend operator fun invoke(name: String, phoneNumber: String) {
        val caller = Contact(phoneNumber = phoneNumber, name = name)
        repository.insertCaller(caller)
    }
}
