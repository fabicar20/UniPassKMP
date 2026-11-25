package edu.unicauca.unipass.plataforma

/**
 * Código que SÍ es distinto en cada plataforma: se declara aquí con `expect`
 * y cada plataforma lo implementa con `actual` (androidMain y desktopMain).
 */

/** Nombre de la plataforma donde corre la app ("Android" o "Escritorio"). */
expect fun nombrePlataforma(): String

/** Hash SHA-256 en hexadecimal (se usa para guardar las contraseñas). */
expect fun sha256(texto: String): String
