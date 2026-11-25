package edu.unicauca.unipass.plataforma

import java.security.MessageDigest

actual fun nombrePlataforma(): String =
    "Escritorio (${System.getProperty("os.name")})"

actual fun sha256(texto: String): String =
    MessageDigest.getInstance("SHA-256")
        .digest(texto.encodeToByteArray())
        .joinToString("") { "%02x".format(it) }
