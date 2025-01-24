package com.amanullah.chromaticsaiassessment.base.navgraph

sealed class Screen(val route: String) {
    data object Contacts : Screen(route = "contacts")
    data object IncomingCall : Screen(route = "incoming_call")
    data object BlockedNumber : Screen(route = "blocked_number")
}