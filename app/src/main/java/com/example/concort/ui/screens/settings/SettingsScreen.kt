package com.example.concort.ui.screens.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.concort.ui.components.ConcortCard
import com.example.concort.ui.theme.ConcortColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Notification toggles
    var matchNotifications by remember { mutableStateOf(true) }
    var messageNotifications by remember { mutableStateOf(true) }
    var queueUpdates by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    // Delete account confirmation
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { 
                Text("Delete Account", color = ConcortColors.Error) 
            },
            text = { 
                Text(
                    "This action cannot be undone. All your data will be permanently deleted.",
                    color = ConcortColors.OnSurfaceVariant
                ) 
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteAccount()
                }) {
                    Text("Delete", color = ConcortColors.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
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
        TopAppBar(
            title = { Text("Settings", color = ConcortColors.OnBackground) },
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
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Notifications Section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400, delayMillis = 100)) + slideInVertically { 20 }
            ) {
                Column {
                    SectionTitle("Notifications")
                    ConcortCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            ToggleSetting(
                                icon = Icons.Default.Favorite,
                                title = "Match Notifications",
                                subtitle = "Get notified when you're matched",
                                isChecked = matchNotifications,
                                onToggle = { matchNotifications = it }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            ToggleSetting(
                                icon = Icons.Default.Message,
                                title = "Message Notifications",
                                subtitle = "Get notified for new messages",
                                isChecked = messageNotifications,
                                onToggle = { messageNotifications = it }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            ToggleSetting(
                                icon = Icons.Default.Schedule,
                                title = "Queue Updates",
                                subtitle = "Get notified about rank changes",
                                isChecked = queueUpdates,
                                onToggle = { queueUpdates = it }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Privacy Section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400, delayMillis = 200)) + slideInVertically { 20 }
            ) {
                Column {
                    SectionTitle("Privacy & Security")
                    ConcortCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Privacy Policy",
                                onClick = { /* Open privacy policy */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Description,
                                title = "Terms of Service",
                                onClick = { /* Open terms */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Storage,
                                title = "Data & Storage",
                                onClick = { /* Open data settings */ }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Support Section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400, delayMillis = 300)) + slideInVertically { 20 }
            ) {
                Column {
                    SectionTitle("Support")
                    ConcortCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SettingsItem(
                                icon = Icons.Default.Help,
                                title = "Help Center",
                                onClick = { /* Open help */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Email,
                                title = "Contact Us",
                                onClick = { /* Open email */ }
                            )
                            HorizontalDivider(color = ConcortColors.Outline.copy(alpha = 0.3f))
                            SettingsItem(
                                icon = Icons.Default.Star,
                                title = "Rate Concort",
                                onClick = { /* Open play store */ }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Danger Zone
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400, delayMillis = 400)) + slideInVertically { 20 }
            ) {
                Column {
                    SectionTitle("Danger Zone")
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = ConcortColors.Error.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { showDeleteDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = ConcortColors.Error,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Delete Account",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ConcortColors.Error
                                )
                                Text(
                                    text = "Permanently delete your account and data",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ConcortColors.Error.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Version info
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400, delayMillis = 500))
            ) {
                Text(
                    text = "Concort v1.0.0 • Made with ❤️",
                    style = MaterialTheme.typography.bodySmall,
                    color = ConcortColors.OnSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = ConcortColors.OnBackground,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun ToggleSetting(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ConcortColors.Primary,
                checkedTrackColor = ConcortColors.Primary.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
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
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = ConcortColors.OnBackground,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ConcortColors.OnSurfaceVariant
        )
    }
}
