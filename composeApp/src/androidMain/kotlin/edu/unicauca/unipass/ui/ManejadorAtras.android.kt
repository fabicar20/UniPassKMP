package edu.unicauca.unipass.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/** Android: el botón/gesto "atrás" del sistema vuelve a la pantalla anterior. */
@Composable
actual fun ManejadorAtras(habilitado: Boolean, alAtras: () -> Unit) {
    BackHandler(enabled = habilitado, onBack = alAtras)
}
