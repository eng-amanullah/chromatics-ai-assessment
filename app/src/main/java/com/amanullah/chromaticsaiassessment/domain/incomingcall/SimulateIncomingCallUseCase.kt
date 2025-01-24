package com.amanullah.chromaticsaiassessment.domain.incomingcall

import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.data.repository.IncomingCallRepository
import javax.inject.Inject

open class SimulateIncomingCallUseCase @Inject constructor(
    private val repository: IncomingCallRepository
) {
    open suspend operator fun invoke(phoneNumber: String): Contact? {
        return repository.getCallerByPhoneNumber(phoneNumber)
    }
}
