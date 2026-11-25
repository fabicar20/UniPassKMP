package edu.unicauca.unipass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.unicauca.unipass.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val contenedor = (application as UniPassApplication).contenedor
        setContent {
            App(contenedor)
        }
    }
}
