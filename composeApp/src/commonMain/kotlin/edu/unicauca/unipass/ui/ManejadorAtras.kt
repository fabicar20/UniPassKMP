package edu.unicauca.unipass.ui

import androidx.compose.runtime.Composable

/**
 * Reacciona al botón "atrás" del sistema. Es distinto en cada plataforma,
 * por eso se declara con `expect` y se implementa con `actual` en androidMain y desktopMain.
 */
@Composable
expect fun ManejadorAtras(habilitado: Boolean, alAtras: () -> Unit)
