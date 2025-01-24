package com.amanullah.chromaticsaiassessment.presentation.contacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amanullah.chromaticsaiassessment.base.ui.ContactItemView
import com.amanullah.chromaticsaiassessment.base.ui.SearchBar
import com.amanullah.chromaticsaiassessment.base.utils.keyboardState
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(viewModel: ContactsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle

    val isPermissionDenied = viewModel.isPermissionDenied
    val isPermissionPermanentlyDenied = viewModel.isPermissionPermanentlyDenied

    // Observe the Paging flow from the ViewModel
    val contacts = viewModel.contactPagingFlow.collectAsLazyPagingItems()
    val searchContacts = viewModel.searchPagingFlow.value.collectAsLazyPagingItems()

    val searchQuery = viewModel.searchQuery

    val coroutineScope = rememberCoroutineScope()

    // Permission launcher for READ_CONTACTS, READ_CALL_LOG, READ_PHONE_STATE
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isContactsGranted = permissions[Manifest.permission.READ_CONTACTS] == true
        val isCallLogGranted = permissions[Manifest.permission.READ_CALL_LOG] == true
        val isPhoneStateGranted = permissions[Manifest.permission.READ_PHONE_STATE] == true

        if (isContactsGranted && isCallLogGranted && isPhoneStateGranted) {
            viewModel.fetchContacts() // Fetch contacts if all permissions are granted
        } else {
            handlePermissionDenial(context, viewModel)
        }
    }

// Launch permission request or fetch contacts on first render
    LaunchedEffect(key1 = Unit) {
        val arePermissionsGranted = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE
        ).all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (arePermissionsGranted) {
            viewModel.fetchContacts()
        } else if (!isPermissionDenied) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE
                )
            )
        }
    }

// Observe lifecycle changes to refresh permission state
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val arePermissionsGranted = listOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE
                ).all { permission ->
                    ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                }

                if (arePermissionsGranted) {
                    viewModel.fetchContacts()
                } else if (isPermissionPermanentlyDenied) {
                    viewModel.onPermissionPermanentlyDenied()
                } else {
                    viewModel.onPermissionDenied()
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    // UI Layout
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Contacts")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            if (isPermissionDenied) {
                PermissionDeniedScreen(
                    context = context,
                    permissionLauncher = permissionLauncher,
                    isPermanentlyDenied = isPermissionPermanentlyDenied
                )
            } else {
                if (!contacts.itemSnapshotList.isEmpty()) {
                    SearchBar(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(query = it) }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No contact found")
                    }
                }

                ContactList(
                    contacts = if (searchQuery.isEmpty()) contacts else searchContacts,
                    onContactAction = { contact ->
                        contact.isBlocked = !contact.isBlocked
                        coroutineScope.launch { viewModel.blockOrUnblockNumber(contact = contact) }
                    }
                )
            }
        }
    }
}

@Composable
fun ContactList(
    contacts: LazyPagingItems<Contact>,
    onContactAction: (Contact) -> Unit
) {
    val isKeyboardOpen by keyboardState()

    AnimatedVisibility(visible = true) {
        if (contacts.itemCount > 0) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(contacts.itemCount) { index ->
                    contacts[index]?.let { item ->
                        ContactItemView(
                            contact = item,
                            clickCallBack = {
                                onContactAction(item)
                            }
                        )
                    }
                }

                // Handling Pagination states
                when {
                    contacts.loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }

                    contacts.loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }

                    contacts.loadState.refresh is LoadState.Error -> {
                        val e = contacts.loadState.refresh as LoadState.Error
                        item {
                            Text(
                                text = "Error loading contacts: ${e.error.localizedMessage}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (isKeyboardOpen) Alignment.TopCenter else Alignment.Center
            ) {
                Text(text = "No contact found")
            }
        }
    }
}

@Composable
fun PermissionDeniedScreen(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    isPermanentlyDenied: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isPermanentlyDenied) {
                    "Permissions are permanently denied. Please enable them in settings."
                } else {
                    "Permissions denied. Grant permissions to access contacts, call logs, and phone state."
                },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (isPermanentlyDenied) {
                    openAppSettings(context)
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE
                        )
                    )
                }
            }) {
                Text(text = if (isPermanentlyDenied) "Open Settings" else "Grant Permissions")
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}

fun handlePermissionDenial(context: Context, viewModel: ContactsViewModel) {
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        context as Activity,
        Manifest.permission.READ_CONTACTS
    )
    if (showRationale) {
        viewModel.onPermissionDenied()
    } else {
        viewModel.onPermissionPermanentlyDenied()
    }
}

@Preview
@Composable
private fun Preview() {
    ContactsScreen()
}