package com.example.concort.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    // Splash
    data object Splash : Screen("splash")
    
    // Auth Flow
    data object Welcome : Screen("welcome")
    data object Register : Screen("register")
    data object OtpVerification : Screen("otp_verification/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_verification/$phoneNumber"
    }
    data object ProfileSetup : Screen("profile_setup")
    
    // Main App
    data object Home : Screen("home")
    data object WaitingList : Screen("waiting_list")
    data object MatchFound : Screen("match_found/{matchId}") {
        fun createRoute(matchId: String) = "match_found/$matchId"
    }
    data object Chat : Screen("chat/{matchId}") {
        fun createRoute(matchId: String) = "chat/$matchId"
    }
    
    // Profile & Settings
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}
