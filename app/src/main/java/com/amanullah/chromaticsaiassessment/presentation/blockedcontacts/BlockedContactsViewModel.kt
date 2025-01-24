package com.amanullah.chromaticsaiassessment.presentation.blockedcontacts

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanullah.chromaticsaiassessment.data.local.entity.BlockedContact
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.FetchBlockedNumbersUseCase
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.SearchBlockedContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.blockedcontacts.UnblockContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedContactsViewModel @Inject constructor(
    private val fetchBlockedNumbersUseCase: FetchBlockedNumbersUseCase,
    private val searchBlockedContactsUseCase: SearchBlockedContactsUseCase,
    private val unblockContactUseCase: UnblockContactUseCase
) : ViewModel() {

    var searchQuery by mutableStateOf(value = "")
        private set

    private val _blockedContacts = mutableStateOf<List<BlockedContact>>(value = emptyList())
    val blockedContacts: State<List<BlockedContact>> = _blockedContacts

    private val _searchBlockedContacts = mutableStateOf<List<BlockedContact>>(value = emptyList())
    val searchBlockedContacts: State<List<BlockedContact>> = _searchBlockedContacts

    init {
        fetchBlockedNumbers()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchBlockedNumbers(query)
    }

    // Fetch all blocked numbers
    fun fetchBlockedNumbers() {
        viewModelScope.launch {
            _blockedContacts.value = fetchBlockedNumbersUseCase()
        }
    }

    // Search blocked contacts
    private fun searchBlockedNumbers(query: String) {
        viewModelScope.launch {
            _searchBlockedContacts.value = if (query.isNotBlank()) {
                searchBlockedContactsUseCase(query)
            } else {
                _blockedContacts.value
            }
        }
    }

    // Unblock a contact
    fun unblockContact(contact: BlockedContact) {
        viewModelScope.launch {
            unblockContactUseCase(contact)
            fetchBlockedNumbers()
        }
    }
}