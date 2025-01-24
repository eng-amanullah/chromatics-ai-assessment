package com.amanullah.chromaticsaiassessment.presentation.incomingcall.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanullah.chromaticsaiassessment.base.ui.SearchBar
import com.amanullah.chromaticsaiassessment.base.utils.isValidBangladeshPhoneNumber

@Composable
fun PopupForAddNumber(
    onDismiss: (String?, String?) -> Unit
) {
    var name by remember { mutableStateOf(value = "") }
    var phoneNumber by remember { mutableStateOf(value = "") }

    var nameError by remember { mutableStateOf(value = "") }
    var phoneNumberError by remember { mutableStateOf(value = "") }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        onDismissRequest = {
            onDismiss(null, null)
        },
        title = {
            Text(
                text = "Add Number to room db for detection",
                style = MaterialTheme.typography.titleSmall
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    searchQuery = name,
                    hint = "Enter name",
                    leadingIcon = {
                        Icons.Default.Person
                    },
                    imeAction = ImeAction.Next,
                    onSearchQueryChange = { name = it }
                )

                AnimatedVisibility(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    visible = nameError.isNotEmpty()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = nameError,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }

                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    searchQuery = phoneNumber,
                    hint = "Enter mobile number",
                    trailingIcon = {
                        Icons.Default.Phone
                    },
                    keyboardType = KeyboardType.Number,
                    onSearchQueryChange = { phoneNumber = it }
                )

                AnimatedVisibility(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    visible = phoneNumberError.isNotEmpty()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = phoneNumberError,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.length < 4) {
                        nameError = "Need at least 4 Character"
                    } else if (!phoneNumber.isValidBangladeshPhoneNumber()) {
                        nameError = ""
                        phoneNumberError = "Invalid mobile number"
                    } else {
                        phoneNumberError = ""
                        onDismiss(name, phoneNumber)
                    }
                }
            ) {
                Text(text = "Save")
            }
        }
    )
}