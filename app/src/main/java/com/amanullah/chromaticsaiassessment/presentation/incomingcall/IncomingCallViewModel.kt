package com.amanullah.chromaticsaiassessment.presentation.incomingcall

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.domain.incomingcall.InsertCallerUseCase
import com.amanullah.chromaticsaiassessment.domain.incomingcall.SimulateIncomingCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomingCallViewModel @Inject constructor(
    private val simulateIncomingCallUseCase: SimulateIncomingCallUseCase,
    private val insertCallerUseCase: InsertCallerUseCase
) : ViewModel() {

    // State to hold the matched caller
    var matchedCaller by mutableStateOf<Contact?>(value = null)

    // Simulate an incoming call with a phone number
    fun simulateIncomingCall(phoneNumber: String) {
        viewModelScope.launch {
            matchedCaller = simulateIncomingCallUseCase(phoneNumber)
        }
    }

    // Insert a new caller using the use case
    fun insertCaller(name: String, phoneNumber: String) {
        viewModelScope.launch {
            insertCallerUseCase(name, phoneNumber)
        }
    }

    // Clear matched caller information
    fun clearCallerInfo() {
        matchedCaller = null
    }
}