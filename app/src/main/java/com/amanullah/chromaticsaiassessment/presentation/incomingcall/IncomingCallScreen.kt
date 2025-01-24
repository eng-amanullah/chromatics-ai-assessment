package com.amanullah.chromaticsaiassessment.presentation.incomingcall

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import com.amanullah.chromaticsaiassessment.presentation.incomingcall.composable.PopupForAddNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingCallScreen(
    incomingMobileNumber: String? = null,
    viewModel: IncomingCallViewModel = hiltViewModel()
) {

    val matchedCaller = viewModel.matchedCaller

    var showAddNumberPopup by remember { mutableStateOf(value = false) }

    if (showAddNumberPopup) {
        PopupForAddNumber { name, number ->
            if (name?.isNotEmpty() == true && number?.isNotEmpty() == true) {
                viewModel.insertCaller(name = name, phoneNumber = number)
            }
            showAddNumberPopup = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Incoming Call")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddNumberPopup = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(key1 = Unit) {
                incomingMobileNumber?.let { viewModel.simulateIncomingCall(it) }
            }

            // If caller is found, show popup with details
            matchedCaller?.let {
                AnimatedVisibility(visible = true) {
                    PopupNotification(
                        callerModel = it,
                        onDismiss = {
                            viewModel.clearCallerInfo()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PopupNotification(callerModel: Contact, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Incoming Call",
                style = MaterialTheme.typography.titleSmall
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Name: ${callerModel.name}"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Number: ${callerModel.phoneNumber}"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Dismiss")
            }
        },
        modifier = Modifier.padding(all = 16.dp)
    )
}