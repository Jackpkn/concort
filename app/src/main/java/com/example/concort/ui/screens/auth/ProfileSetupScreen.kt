package com.example.concort.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.concort.domain.model.Gender
import com.example.concort.ui.components.ConcortButton
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.PremiumGradient
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onNavigateBack: () -> Unit,
    onProfileComplete: (name: String, gender: Gender, age: Int?, city: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var age by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    
    val isFormValid = name.isNotBlank() && selectedGender != null
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
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
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                            text = "Complete your profile",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = ConcortColors.OnBackground
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Tell us a little about yourself",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Name field
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 100)) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(600, delayMillis = 100)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Name *",
                            style = MaterialTheme.typography.labelLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "What should we call you?",
                                    color = ConcortColors.OnSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = ConcortColors.Primary
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConcortColors.Primary,
                                unfocusedBorderColor = ConcortColors.Outline,
                                focusedContainerColor = ConcortColors.SurfaceContainer,
                                unfocusedContainerColor = ConcortColors.SurfaceContainer,
                                cursorColor = ConcortColors.Primary
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Gender selection
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(600, delayMillis = 200)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Gender *",
                            style = MaterialTheme.typography.labelLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GenderOption(
                                gender = Gender.MALE,
                                isSelected = selectedGender == Gender.MALE,
                                onClick = { selectedGender = Gender.MALE },
                                modifier = Modifier.weight(1f)
                            )
                            
                            GenderOption(
                                gender = Gender.FEMALE,
                                isSelected = selectedGender == Gender.FEMALE,
                                onClick = { selectedGender = Gender.FEMALE },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Age field (optional)
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 300)) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(600, delayMillis = 300)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Age (recommended)",
                            style = MaterialTheme.typography.labelLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = age,
                            onValueChange = { 
                                if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                    age = it
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "Your age",
                                    color = ConcortColors.OnSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Cake,
                                    contentDescription = null,
                                    tint = ConcortColors.Secondary
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConcortColors.Primary,
                                unfocusedBorderColor = ConcortColors.Outline,
                                focusedContainerColor = ConcortColors.SurfaceContainer,
                                unfocusedContainerColor = ConcortColors.SurfaceContainer,
                                cursorColor = ConcortColors.Primary
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // City field (optional)
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 400)) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(600, delayMillis = 400)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "City (optional)",
                            style = MaterialTheme.typography.labelLarge,
                            color = ConcortColors.OnSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "Where are you from?",
                                    color = ConcortColors.OnSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = ConcortColors.Tertiary
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConcortColors.Primary,
                                unfocusedBorderColor = ConcortColors.Outline,
                                focusedContainerColor = ConcortColors.SurfaceContainer,
                                unfocusedContainerColor = ConcortColors.SurfaceContainer,
                                cursorColor = ConcortColors.Primary
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600, delayMillis = 500)) + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = tween(600, delayMillis = 500)
                    )
                ) {
                    ConcortButton(
                        text = "Complete Profile",
                        onClick = {
                            isLoading = true
                            onProfileComplete(
                                name,
                                selectedGender!!,
                                age.toIntOrNull(),
                                city.ifBlank { null }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid,
                        isLoading = isLoading
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun GenderOption(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female
    val label = if (gender == Gender.MALE) "Male" else "Female"
    val emoji = if (gender == Gender.MALE) "👨" else "👩"
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(PremiumGradient),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = ConcortColors.Outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            )
            .background(
                if (isSelected) ConcortColors.PrimaryContainer.copy(alpha = 0.3f)
                else ConcortColors.SurfaceContainer
            )
            .clickable { onClick() }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) ConcortColors.Primary else ConcortColors.OnSurfaceVariant
            )
        }
    }
}
