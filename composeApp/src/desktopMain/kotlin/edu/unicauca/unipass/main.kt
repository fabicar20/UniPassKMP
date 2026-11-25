package edu.unicauca.unipass

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import edu.unicauca.unipass.datos.DesktopDriverFactory
import edu.unicauca.unipass.ui.App

/** Punto de entrada en Desktop (Windows, macOS o Linux). */
fun main() = application {
    val contenedor = remember { AppContainer(DesktopDriverFactory()) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "UniPass",
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
    ) {
        App(contenedor)
    }
}
