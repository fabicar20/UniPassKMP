package edu.unicauca.unipass.ui

import androidx.compose.runtime.Composable

/** Desktop: no hay botón "atrás" del sistema; se usa la flecha de la barra superior. */
@Composable
actual fun ManejadorAtras(habilitado: Boolean, alAtras: () -> Unit) {
    // Nada que hacer en escritorio.
}
