package com.example.concort.ui.screens.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.concort.ui.components.ConcortCard
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    phoneNumber: String,
    gender: String,
    age: Int?,
    city: String?,
    queueRank: Int?,
    status: String,
    onNavigateBack: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", color = ConcortColors.OnBackground) },
            text = { Text("Are you sure you want to logout?", color = ConcortColors.OnSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Logout", color = ConcortColors.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = ConcortColors.OnSurfaceVariant)
                }
            },
            containerColor = ConcortColors.Surface
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ConcortColors.Background)
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Profile", color = ConcortColors.OnBackground) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ConcortColors.OnBackground
                    )
                }
            },
            actions = {
                IconButton(onClick = onEditProfile) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = ConcortColors.Primary
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile avatar
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600)) + scaleIn(initialScale = 0.8f)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(brush = Brush.linearGradient(PremiumGradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Name
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 100))
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = ConcortColors.OnBackground
                )
            }
            
            // Phone
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 150))
            ) {
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ConcortColors.OnSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status chip
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 200))
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (status) {
                        "MATCHED" -> ConcortColors.Success.copy(alpha = 0.2f)
                        "WAITING" -> ConcortColors.Tertiary.copy(alpha = 0.2f)
                        else -> ConcortColors.Outline
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (status) {
                                "MATCHED" -> Icons.Default.Favorite
                                "WAITING" -> Icons.Default.Schedule
                                else -> Icons.Default.Person
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = when (status) {
                                "MATCHED" -> ConcortColors.Success
                                "WAITING" -> ConcortColors.Tertiary
                                else -> ConcortColors.OnSurfaceVariant
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (status) {
                                "MATCHED" -> "Matched! 💕"
                                "WAITING" -> "Queue Rank #${queueRank ?: "?"}"
                                else -> status
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = when (status) {
                                "MATCHED" -> ConcortColors.Success
                                "WAITING" -> ConcortColors.Tertiary
                                else -> ConcortColors.OnSurfaceVariant
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Profile info cards
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 300))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileInfoRow(icon = Icons.Default.Person, label = "Gender", value = gender)
                    age?.let {
                        ProfileInfoRow(icon = Icons.Default.Cake, label = "Age", value = "$it years")
                    }
                    city?.let {
                        ProfileInfoRow(icon = Icons.Default.LocationOn, label = "City", value = it)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Settings section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 400))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ConcortColors.OnBackground,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    ConcortCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SettingsItem(
                                icon = Icons.Default.Notifications,
                                title = "Notifications",
                                subtitle = "Manage push notifications",
                                onClick = { /* TODO */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Privacy",
                                subtitle = "Control your data",
                                onClick = { /* TODO */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Help,
                                title = "Help & Support",
                                subtitle = "FAQs and contact us",
                                onClick = { /* TODO */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Info,
                                title = "About",
                                subtitle = "Version 1.0.0",
                                onClick = { /* TODO */ }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Logout button
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(600, delayMillis = 500))
            ) {
                TextButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = ConcortColors.Error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        color = ConcortColors.Error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    ConcortCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ConcortColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = ConcortColors.OnSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = ConcortColors.OnBackground
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ConcortColors.OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = ConcortColors.OnBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ConcortColors.OnSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ConcortColors.OnSurfaceVariant
        )
    }
}
