package com.example.pc2romankelly22200195

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pc2romankelly22200195.presentation.navigation.AppNavGraph
import com.example.pc2romankelly22200195.ui.theme.Pc2RomanKelly22200195Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pc2RomanKelly22200195Theme {
                AppNavGraph()
            }
        }
    }
}