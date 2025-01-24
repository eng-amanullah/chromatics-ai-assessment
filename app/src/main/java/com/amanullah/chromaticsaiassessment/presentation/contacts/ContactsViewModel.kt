package com.amanullah.chromaticsaiassessment.presentation.contacts

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.domain.contacts.BlockOrUnblockContactUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.CacheContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.FetchContactsUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.GetContactsPagedUseCase
import com.amanullah.chromaticsaiassessment.domain.contacts.SearchContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    application: Application,
    private val getContactsPagedUseCase: GetContactsPagedUseCase,
    private val fetchContactsUseCase: FetchContactsUseCase,
    private val cacheContactsUseCase: CacheContactsUseCase,
    private val searchContactsUseCase: SearchContactsUseCase,
    private val blockOrUnblockContactUseCase: BlockOrUnblockContactUseCase
) : AndroidViewModel(application) {

    val _filteredContacts = mutableStateOf<List<Contact>>(emptyList())

    var searchQuery by mutableStateOf(value = "")
        private set

    var isPermissionDenied by mutableStateOf(value = false)
        private set

    var isPermissionPermanentlyDenied by mutableStateOf(value = false)
        private set

    // Paging flow for contacts
    val contactPagingFlow = getContactsPagedUseCase()

    // Paging flow for search
    val searchPagingFlow =
        mutableStateOf<Flow<PagingData<Contact>>>(value = flowOf(PagingData.empty()))

    fun fetchContacts() {
        viewModelScope.launch {
            val isGranted = ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                val fetchedContacts = withContext(context = Dispatchers.IO) {
                    fetchContactsUseCase()
                }
                cacheContactsUseCase(fetchedContacts)
                _filteredContacts.value = fetchedContacts
                isPermissionDenied = false
            } else {
                onPermissionDenied()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchContacts(query)
    }

    private fun searchContacts(query: String) {
        searchPagingFlow.value = searchContactsUseCase(query)
    }

    fun onPermissionDenied() {
        isPermissionDenied = true
        isPermissionPermanentlyDenied = false
    }

    fun onPermissionPermanentlyDenied() {
        isPermissionDenied = true
        isPermissionPermanentlyDenied = true
    }

    fun blockOrUnblockNumber(contact: Contact) {
        viewModelScope.launch {
            blockOrUnblockContactUseCase(contact)
        }
    }
}