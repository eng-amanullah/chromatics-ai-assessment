package com.amanullah.chromaticsaiassessment.presentation.blockedcontacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.base.ui.ContactItemView
import com.amanullah.chromaticsaiassessment.base.ui.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedContactsScreen(viewModel: BlockedContactsViewModel = hiltViewModel()) {
    val blockedContacts = viewModel.blockedContacts.value
    val searchBlockedContacts = viewModel.searchBlockedContacts.value

    val searchQuery = viewModel.searchQuery

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Blocked Contacts")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            if (blockedContacts.isNotEmpty()) {
                SearchBar(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(query = it) }
                )

                AnimatedVisibility(visible = true) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(if (searchQuery.isEmpty()) blockedContacts else searchBlockedContacts) { contact ->
                            ContactItemView(
                                contact = Contact(
                                    phoneNumber = contact.phoneNumber,
                                    name = contact.name,
                                    photoUri = contact.photoUri,
                                    isBlocked = contact.isBlocked
                                ),
                                trailingIconImageVector = Icons.Default.Delete,
                                clickCallBack = {
                                    viewModel.unblockContact(contact)
                                }
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No block contact")
                }
            }
        }
    }
}