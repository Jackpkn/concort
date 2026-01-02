package com.example.concort.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.concort.domain.model.QueueStatus
import com.example.concort.ui.components.ConcortButton
import com.example.concort.ui.components.ConcortCard
import com.example.concort.ui.components.RankCard
import com.example.concort.ui.components.StatsChip
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import com.example.concort.ui.theme.SuccessGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    queueStatus: QueueStatus?,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDemoMatch: () -> Unit = {} // Demo mode callback
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
        // Animated background orbs
        AnimatedBackgroundOrbs()
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = ConcortColors.Primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Concort",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ConcortColors.OnBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = ConcortColors.OnSurfaceVariant
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = ConcortColors.OnSurfaceVariant
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Welcome message
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
                            text = "Hey $userName! 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = ConcortColors.OnBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You're in the queue for a meaningful connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Rank card
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 200)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(600, delayMillis = 200)
                    )
                ) {
                    queueStatus?.let { status ->
                        RankCard(
                            rank = status.rank,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 🎮 DEMO MODE BUTTON - See Match & Chat screens
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 250)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(600, delayMillis = 250)
                    )
                ) {
                    ConcortButton(
                        text = "🎮 Demo: Simulate Match!",
                        onClick = onDemoMatch,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = SuccessGradient
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Info message
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 300))
                ) {
                    ConcortCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = ConcortColors.Info,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "You'll be matched when your turn arrives. We'll notify you instantly!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ConcortColors.OnSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Queue stats
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 400))
                ) {
                    queueStatus?.let { status ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Queue Stats",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = ConcortColors.OnBackground,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatsChip(
                                    label = "👨 Males",
                                    value = "${status.malesWaiting}",
                                    modifier = Modifier.weight(1f)
                                )
                                StatsChip(
                                    label = "👩 Females",
                                    value = "${status.femalesWaiting}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Tips card
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 500))
                ) {
                    ConcortCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💡",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Pro Tip",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ConcortColors.Tertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Keep the app updated and notifications on to never miss your match moment!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ConcortColors.OnSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // How it works section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 600))
                ) {
                    HowItWorksSection()
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundOrbs() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_orbs")
    
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_offset"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-right orb
        Box(
            modifier = Modifier
                .offset(x = (280 + offset * 40).dp, y = (100 - offset * 30).dp)
                .size(200.dp)
                .blur(80.dp)
                .alpha(0.25f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ConcortColors.Primary, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        
        // Bottom-left orb
        Box(
            modifier = Modifier
                .offset(x = (-80 + offset * 50).dp, y = (600 - offset * 40).dp)
                .size(250.dp)
                .blur(100.dp)
                .alpha(0.2f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ConcortColors.Secondary, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun HowItWorksSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "How It Works",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ConcortColors.OnBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        val steps = listOf(
            Triple("1️⃣", "Wait in Queue", "Your rank moves up as matches happen"),
            Triple("2️⃣", "Get Matched", "First-come, first-matched fairly"),
            Triple("3️⃣", "Start Chatting", "Connect safely in our app"),
            Triple("4️⃣", "Meet IRL", "When you're both ready!")
        )
        
        steps.forEachIndexed { index, (emoji, title, subtitle) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = ConcortColors.OnBackground
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = ConcortColors.OnSurfaceVariant
                    )
                }
            }
            
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .width(2.dp)
                        .height(16.dp)
                        .background(ConcortColors.Outline)
                )
            }
        }
    }
}
