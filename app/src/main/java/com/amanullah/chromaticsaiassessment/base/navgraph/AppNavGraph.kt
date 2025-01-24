package com.amanullah.chromaticsaiassessment.base.navgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amanullah.chromaticsaiassessment.R
import com.amanullah.chromaticsaiassessment.presentation.blockedcontacts.BlockedContactsScreen
import com.amanullah.chromaticsaiassessment.presentation.contacts.ContactsScreen
import com.amanullah.chromaticsaiassessment.presentation.incomingcall.IncomingCallScreen

@Composable
fun AppNavGraph(incomingMobileNumber: String?) {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(weight = 1F)
        ) {
            NavHost(
                navController = navController,
                startDestination = if (incomingMobileNumber.isNullOrEmpty()) Screen.Contacts.route else Screen.IncomingCall.route,
                modifier = Modifier
            ) {
                composable(Screen.Contacts.route) {
                    ContactsScreen()
                }
                composable(Screen.IncomingCall.route) {
                    IncomingCallScreen(incomingMobileNumber)
                }
                composable(Screen.BlockedNumber.route) {
                    BlockedContactsScreen()
                }
            }
        }

        BottomNavBar(navController = navController)
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val currentRoute = navBackStackEntry.value?.destination?.route

        // Contacts Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_contacts),
                    contentDescription = null,
                    tint = if (currentRoute == Screen.Contacts.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Contacts",
                    color = if (currentRoute == Screen.Contacts.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.Contacts.route,
            onClick = {
                if (currentRoute != Screen.Contacts.route) {
                    navController.navigate(Screen.Contacts.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Screen.Contacts.route) { inclusive = false }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.Contacts.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.Contacts.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )

        // Incoming Call Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = if (currentRoute == Screen.IncomingCall.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Incoming",
                    color = if (currentRoute == Screen.IncomingCall.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.IncomingCall.route,
            onClick = {
                if (currentRoute != Screen.IncomingCall.route) {
                    navController.navigate(Screen.IncomingCall.route) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(Screen.IncomingCall.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.IncomingCall.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.IncomingCall.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )

        // Blocked Numbers Tab
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_block),
                    contentDescription = null,
                    tint = if (currentRoute == Screen.BlockedNumber.route) {
                        MaterialTheme.colorScheme.primary // Selected icon color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default icon color
                    }
                )
            },
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Blocked",
                    color = if (currentRoute == Screen.BlockedNumber.route) {
                        MaterialTheme.colorScheme.primary // Selected label color
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant // Default label color
                    }
                )
            },
            selected = currentRoute == Screen.BlockedNumber.route,
            onClick = {
                if (currentRoute != Screen.BlockedNumber.route) {
                    navController.navigate(Screen.BlockedNumber.route) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(Screen.BlockedNumber.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .background(
                    color = if (currentRoute == Screen.BlockedNumber.route) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) // Background when selected
                    } else {
                        Color.Transparent
                    }
                )
                .scale(
                    if (currentRoute == Screen.BlockedNumber.route) 1.1f else 1f // Slight scale when selected
                ),
            alwaysShowLabel = true
        )
    }
}

