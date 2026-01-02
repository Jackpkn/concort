package com.example.concort.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concort.ui.components.ConcortButton
import com.example.concort.ui.theme.ConcortColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    onNavigateBack: () -> Unit,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(30) }
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
        delay(500)
        focusRequester.requestFocus()
    }
    
    // Countdown timer for resend
    LaunchedEffect(resendCountdown) {
        if (resendCountdown > 0) {
            delay(1000)
            resendCountdown--
        }
    }
    
    // Auto-submit when 6 digits entered
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6) {
            isLoading = true
            delay(500)
            onVerifyOtp(otpCode)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConcortColors.Background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ConcortColors.OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600)) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(600)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Verify your number",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = ConcortColors.OnBackground
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Enter the 6-digit code sent to",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = phoneNumber,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = ConcortColors.Primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 200)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(600, delayMillis = 200)
                    )
                ) {
                    // OTP input boxes
                    OtpInputField(
                        otpCode = otpCode,
                        onOtpChange = { 
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                otpCode = it
                            }
                        },
                        focusRequester = focusRequester
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Resend section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 400))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (resendCountdown > 0) {
                            Text(
                                text = "Resend code in ${resendCountdown}s",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ConcortColors.OnSurfaceVariant
                            )
                        } else {
                            TextButton(
                                onClick = {
                                    resendCountdown = 30
                                    onResendOtp()
                                }
                            ) {
                                Text(
                                    text = "Resend Code",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ConcortColors.Primary
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 500)) + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = tween(600, delayMillis = 500)
                    )
                ) {
                    ConcortButton(
                        text = "Verify",
                        onClick = {
                            isLoading = true
                            onVerifyOtp(otpCode)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = otpCode.length == 6,
                        isLoading = isLoading
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun OtpInputField(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    BasicTextField(
        value = otpCode,
        onValueChange = onOtpChange,
        modifier = Modifier.focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(6) { index ->
                    val char = otpCode.getOrNull(index)
                    val isFocused = otpCode.length == index
                    
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = if (char != null) ConcortColors.PrimaryContainer 
                                        else ConcortColors.SurfaceContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = when {
                                    isFocused -> ConcortColors.Primary
                                    char != null -> ConcortColors.Primary.copy(alpha = 0.5f)
                                    else -> ConcortColors.Outline
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char?.toString() ?: "",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = ConcortColors.OnBackground
                            )
                        )
                    }
                }
            }
        }
    )
}
