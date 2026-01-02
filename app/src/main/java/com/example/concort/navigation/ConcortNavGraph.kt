package com.example.concort.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.concort.domain.model.Gender
import com.example.concort.domain.model.Match
import com.example.concort.domain.model.Message
import com.example.concort.domain.model.QueueStatus
import com.example.concort.ui.screens.auth.OtpVerificationScreen
import com.example.concort.ui.screens.auth.ProfileSetupScreen
import com.example.concort.ui.screens.auth.RegisterScreen
import com.example.concort.ui.screens.auth.WelcomeScreen
import com.example.concort.ui.screens.chat.ChatScreen
import com.example.concort.ui.screens.home.HomeScreen
import com.example.concort.ui.screens.match.MatchFoundScreen
import com.example.concort.ui.screens.profile.ProfileScreen
import com.example.concort.ui.screens.settings.SettingsScreen
import com.example.concort.ui.screens.splash.SplashScreen
import com.example.concort.ui.viewmodel.AuthViewModel
import com.example.concort.ui.viewmodel.HomeViewModel

@Composable
fun ConcortNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route  // Start with splash
) {
    // Shared auth ViewModel for auth flow
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()
    
    // User data
    var currentUser by remember { mutableStateOf<CurrentUserData?>(null) }
    var mockMessages by remember { mutableStateOf(listOf<Message>()) }
    
    val mockQueueStatus = QueueStatus(
        rank = 7,
        estimatedWaitTime = "~2 days",
        malesWaiting = 45,
        femalesWaiting = 32
    )
    
    val mockMatch = Match(
        id = "match_1",
        partnerId = "partner_1",
        partnerName = "Sarah",
        partnerAge = 25,
        partnerCity = "Mumbai"
    )
    
    // Pre-populated demo messages
    val demoMessages = listOf(
        Message(
            id = "1",
            matchId = mockMatch.id,
            senderId = "partner_1",
            content = "Hi there! Nice to match with you 😊",
            sentAt = System.currentTimeMillis() - 3600000,
            isSentByMe = false
        ),
        Message(
            id = "2",
            matchId = mockMatch.id,
            senderId = "current_user",
            content = "Hey Sarah! So glad we matched. How's your day going?",
            sentAt = System.currentTimeMillis() - 3000000,
            isSentByMe = true
        ),
        Message(
            id = "3",
            matchId = mockMatch.id,
            senderId = "partner_1",
            content = "Pretty good! Just finished work. I love that this app doesn't have swiping 😄",
            sentAt = System.currentTimeMillis() - 2400000,
            isSentByMe = false
        )
    )
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Welcome screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.Register.route)
                },
                onLogin = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        // Register screen
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSendOtp = { phoneNumber ->
                    authViewModel.register(phoneNumber)
                    navController.navigate(Screen.OtpVerification.createRoute(phoneNumber))
                }
            )
        }
        
        // OTP Verification
        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            
            LaunchedEffect(authState.isVerified) {
                if (authState.isVerified) {
                    if (authState.isProfileComplete) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = false }
                        }
                    }
                }
            }
            
            OtpVerificationScreen(
                phoneNumber = phoneNumber,
                onNavigateBack = { navController.popBackStack() },
                onVerifyOtp = { otp -> authViewModel.verifyOtp(phoneNumber, otp) },
                onResendOtp = { authViewModel.register(phoneNumber) }
            )
        }
        
        // Profile Setup
        composable(Screen.ProfileSetup.route) {
            LaunchedEffect(authState.isProfileComplete) {
                if (authState.isProfileComplete && authState.isVerified) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            }
            
            ProfileSetupScreen(
                onNavigateBack = { navController.popBackStack() },
                onProfileComplete = { name, gender, age, city ->
                    currentUser = CurrentUserData(name, gender.name, "+919999999999", age, city, null, "WAITING")
                    authViewModel.setupProfile(name, gender.name, age, city)
                }
            )
        }
        
        // Home screen
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeState by homeViewModel.uiState.collectAsState()
            
            HomeScreen(
                userName = currentUser?.name ?: homeState.userName,
                queueStatus = homeState.queueStatus?.let {
                    QueueStatus(
                        rank = it.rank,
                        estimatedWaitTime = it.estimatedWaitTime,
                        malesWaiting = it.malesWaiting,
                        femalesWaiting = it.femalesWaiting
                    )
                } ?: mockQueueStatus,
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onDemoMatch = {
                    navController.navigate(Screen.MatchFound.createRoute(mockMatch.id))
                }
            )
        }
        
        // Profile screen
        composable(Screen.Profile.route) {
            ProfileScreen(
                userName = currentUser?.name ?: "User",
                phoneNumber = currentUser?.phoneNumber ?: "+91 XXXX XXXX",
                gender = currentUser?.gender ?: "Not set",
                age = currentUser?.age,
                city = currentUser?.city,
                queueRank = currentUser?.queueRank,
                status = currentUser?.status ?: "WAITING",
                onNavigateBack = { navController.popBackStack() },
                onEditProfile = { /* TODO: Edit profile */ },
                onLogout = {
                    authViewModel.resetState()
                    currentUser = null
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Settings screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onDeleteAccount = {
                    // TODO: Delete account API
                    authViewModel.resetState()
                    currentUser = null
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Match Found screen
        composable(
            route = Screen.MatchFound.route,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { 
            MatchFoundScreen(
                match = mockMatch,
                onStartChatting = {
                    mockMessages = demoMessages
                    navController.navigate(Screen.Chat.createRoute(mockMatch.id)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onDismiss = { navController.popBackStack() }
            )
        }
        
        // Chat screen
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { 
            ChatScreen(
                match = mockMatch,
                messages = mockMessages,
                currentUserId = "current_user",
                onNavigateBack = { navController.popBackStack() },
                onSendMessage = { content ->
                    mockMessages = mockMessages + Message(
                        matchId = mockMatch.id,
                        senderId = "current_user",
                        content = content,
                        isSentByMe = true
                    )
                }
            )
        }
    }
}

private data class CurrentUserData(
    val name: String,
    val gender: String,
    val phoneNumber: String,
    val age: Int?,
    val city: String?,
    val queueRank: Int?,
    val status: String
)
