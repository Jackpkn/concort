package com.example.concort.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    
    // Heartbeat animation
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartbeatScale"
    )
    
    // Logo scale animation
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )
    
    // Logo alpha animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800),
        label = "logoAlpha"
    )
    
    // Tagline alpha
    val taglineAlpha by animateFloatAsState(
        targetValue = if (showTagline) 1f else 0f,
        animationSpec = tween(600),
        label = "taglineAlpha"
    )
    
    // Orb animations
    val orbOffset1 by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    
    val orbOffset2 by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )
    
    LaunchedEffect(Unit) {
        delay(300)
        startAnimation = true
        delay(800)
        showTagline = true
        delay(1500)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConcortColors.Background),
        contentAlignment = Alignment.Center
    ) {
        // Animated gradient orbs
        Box(
            modifier = Modifier
                .offset(x = (-100 + orbOffset1).dp, y = (-200 + orbOffset2).dp)
                .size(300.dp)
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ConcortColors.Primary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Box(
            modifier = Modifier
                .offset(x = (120 + orbOffset2).dp, y = (150 + orbOffset1).dp)
                .size(250.dp)
                .blur(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ConcortColors.Secondary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with heartbeat
            Box(
                modifier = Modifier
                    .scale(logoScale * heartbeatScale)
                    .alpha(logoAlpha)
                    .size(140.dp)
                    .background(
                        brush = Brush.linearGradient(PremiumGradient),
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💕",
                    fontSize = 64.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App name
            Text(
                text = "Concort",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = ConcortColors.OnBackground,
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tagline
            Text(
                text = "Your turn to love.",
                style = MaterialTheme.typography.titleMedium,
                color = ConcortColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading indicator
            AnimatedVisibility(
                visible = showTagline,
                enter = fadeIn(tween(400))
            ) {
                Text(
                    text = "Finding real connections...",
                    style = MaterialTheme.typography.bodySmall,
                    color = ConcortColors.OnSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}
