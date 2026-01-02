package com.example.concort.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concort.ui.components.ConcortButton
import com.example.concort.ui.components.ConcortOutlinedButton
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConcortColors.Background)
    ) {
        // Animated gradient orbs in background
        AnimatedGradientOrbs()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            
            // Logo with animation
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800)) + 
                        scaleIn(initialScale = 0.8f, animationSpec = tween(800))
            ) {
                LogoSection()
            }
            
            Spacer(modifier = Modifier.weight(0.1f))
            
            // Tagline
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                        slideInVertically(
                            initialOffsetY = { 50 },
                            animationSpec = tween(800, delayMillis = 200)
                        )
            ) {
                TaglineSection()
            }
            
            Spacer(modifier = Modifier.weight(0.15f))
            
            // Features
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 400))
            ) {
                FeaturesSection()
            }
            
            Spacer(modifier = Modifier.weight(0.2f))
            
            // Buttons
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 600)) +
                        slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = tween(800, delayMillis = 600)
                        )
            ) {
                ButtonsSection(onGetStarted = onGetStarted, onLogin = onLogin)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AnimatedGradientOrbs() {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-right pink orb
        Box(
            modifier = Modifier
                .offset(
                    x = (200 + offset1 * 50).dp,
                    y = ((-50) + offset1 * 30).dp
                )
                .size(300.dp)
                .blur(80.dp)
                .alpha(0.4f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ConcortColors.Primary,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Bottom-left purple orb
        Box(
            modifier = Modifier
                .offset(
                    x = ((-100) + offset2 * 40).dp,
                    y = (500 + offset2 * 50).dp
                )
                .size(350.dp)
                .blur(100.dp)
                .alpha(0.3f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ConcortColors.Secondary,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun LogoSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartbeat"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated heart icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = ConcortColors.Primary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // App name with gradient
        Text(
            text = "Concort",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            ),
            color = ConcortColors.OnBackground
        )
    }
}

@Composable
private fun TaglineSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "A dating platform built on",
            style = MaterialTheme.typography.titleLarge,
            color = ConcortColors.OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "fairness, patience & real connections",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = ConcortColors.Primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No swipes. No chaos. Just your turn.",
            style = MaterialTheme.typography.bodyLarge,
            color = ConcortColors.OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FeaturesSection() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FeatureItem(emoji = "🎯", text = "Fair queue system - first come, first match")
        FeatureItem(emoji = "💬", text = "Safe in-app chat - no number sharing")
        FeatureItem(emoji = "✨", text = "Real connections - no endless swiping")
    }
}

@Composable
private fun FeatureItem(emoji: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = ConcortColors.OnSurfaceVariant
        )
    }
}

@Composable
private fun ButtonsSection(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ConcortButton(
            text = "Get Started",
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth()
        )
        
        ConcortOutlinedButton(
            text = "I already have an account",
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Terms text
        Text(
            text = "By continuing, you agree to our Terms of Service\nand Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = ConcortColors.OnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
