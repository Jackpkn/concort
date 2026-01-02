package com.example.concort.ui.screens.match

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concort.domain.model.Match
import com.example.concort.ui.components.ConcortButton
import com.example.concort.ui.components.ConcortCard
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import com.example.concort.ui.theme.SuccessGradient
import kotlinx.coroutines.delay

@Composable
fun MatchFoundScreen(
    match: Match,
    onStartChatting: () -> Unit,
    onDismiss: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        showConfetti = true
        delay(500)
        showContent = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConcortColors.Background)
    ) {
        // Celebration background effects
        CelebrationBackground(showConfetti)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.15f))
            
            // Celebration emoji
            AnimatedVisibility(
                visible = showConfetti,
                enter = scaleIn(
                    initialScale = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn()
            ) {
                Text(
                    text = "🎉",
                    fontSize = 80.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Main title
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(600)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You've got your",
                        style = MaterialTheme.typography.headlineSmall,
                        color = ConcortColors.OnSurfaceVariant
                    )
                    Text(
                        text = "first match!",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = ConcortColors.Primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Match card
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, delayMillis = 200)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(600, delayMillis = 200)
                )
            ) {
                MatchProfileCard(match = match)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Message
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, delayMillis = 400))
            ) {
                ConcortCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "This match is based on availability and order, not swipes or algorithms. Take this as a real chance to connect.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ConcortColors.OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(0.15f))
            
            // Buttons
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600, delayMillis = 600)) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(600, delayMillis = 600)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ConcortButton(
                        text = "Start Chatting 💬",
                        onClick = onStartChatting,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = SuccessGradient
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CelebrationBackground(show: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        if (show) {
            // Large pink glow
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-100).dp)
                    .size((300 * scale).dp)
                    .blur(120.dp)
                    .alpha(0.4f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ConcortColors.Primary, Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
            
            // Purple glow
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 100.dp, y = 100.dp)
                    .size((200 * scale).dp)
                    .blur(100.dp)
                    .alpha(0.3f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ConcortColors.Secondary, Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
            
            // Gold accent
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-80).dp, y = 150.dp)
                    .size((150 * scale).dp)
                    .blur(80.dp)
                    .alpha(0.25f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(ConcortColors.Tertiary, Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun MatchProfileCard(match: Match) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(PremiumGradient)
            )
            .padding(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(ConcortColors.SurfaceContainer)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(PremiumGradient)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = match.partnerName.first().uppercase(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Name
            Text(
                text = match.partnerName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ConcortColors.OnBackground
            )
            
            // Age and city
            if (match.partnerAge != null || match.partnerCity != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    match.partnerAge?.let { age ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cake,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ConcortColors.OnSurfaceVariant
                            )
                            Text(
                                text = "$age",
                                style = MaterialTheme.typography.bodyLarge,
                                color = ConcortColors.OnSurfaceVariant
                            )
                        }
                    }
                    
                    if (match.partnerAge != null && match.partnerCity != null) {
                        Text(
                            text = "•",
                            color = ConcortColors.OnSurfaceVariant
                        )
                    }
                    
                    match.partnerCity?.let { city ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ConcortColors.OnSurfaceVariant
                            )
                            Text(
                                text = city,
                                style = MaterialTheme.typography.bodyLarge,
                                color = ConcortColors.OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
