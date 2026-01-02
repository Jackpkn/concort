package com.example.concort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.concort.navigation.ConcortNavGraph
import com.example.concort.ui.theme.ConcortColors
import com.example.concort.ui.theme.ConcortTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ConcortTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ConcortColors.Background
                ) {
                    val navController = rememberNavController()
                    ConcortNavGraph(navController = navController)
                }
            }
        }
    }
}