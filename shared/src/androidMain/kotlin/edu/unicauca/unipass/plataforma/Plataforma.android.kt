package edu.unicauca.unipass.plataforma

import android.os.Build
import java.security.MessageDigest


//

actual fun nombrePlataforma(): String = "Android ${Build.VERSION.RELEASE}"

actual fun sha256(texto: String): String =
    MessageDigest.getInstance("SHA-256")
        .digest(texto.encodeToByteArray())
        .joinToString("") { "%02x".format(it) }
