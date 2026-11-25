package edu.unicauca.unipass

import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import edu.unicauca.unipass.datos.DesktopDriverFactory
import edu.unicauca.unipass.ui.App
import org.jetbrains.skia.Image

/** Punto de entrada en Desktop (Windows, macOS o Linux). */
fun main() = application {
    val contenedor = remember { AppContainer(DesktopDriverFactory()) }
    val icono = remember { cargarIcono() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "UniPass",
        icon = icono,   // icono de la ventana y de la barra de tareas
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
    ) {
        App(contenedor)
    }
}

/** Lee `icono.png` de `desktopMain/resources`. Si no se encuentra, la ventana usa el icono por defecto. */
private fun cargarIcono(): BitmapPainter? {
    val bytes = Thread.currentThread().contextClassLoader
        .getResourceAsStream("icono.png")
        ?.use { it.readBytes() }
        ?: return null
    return BitmapPainter(Image.makeFromEncoded(bytes).toComposeImageBitmap())
}
