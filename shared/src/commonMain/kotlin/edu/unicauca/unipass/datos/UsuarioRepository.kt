package edu.unicauca.unipass.datos

import edu.unicauca.unipass.db.UniPassDatabase
import edu.unicauca.unipass.db.Usuario as UsuarioFila
import edu.unicauca.unipass.dominio.Usuario
import edu.unicauca.unipass.plataforma.sha256

/** Registro y autenticación de usuarios. La contraseña se guarda como hash SHA-256. */
class UsuarioRepository(baseDeDatos: UniPassDatabase) {

    private val consultas = baseDeDatos.usuarioQueries

    /** Devuelve el usuario creado, o null si ya existe una cuenta con ese correo. */
    fun registrar(nombre: String, correo: String, clave: String): Usuario? {
        if (consultas.buscarPorCorreo(correo).executeAsOneOrNull() != null) return null
        consultas.insertar(nombre, correo, hash(correo, clave))
        return consultas.buscarPorCorreo(correo).executeAsOneOrNull()?.aDominio()
    }

    /** Devuelve el usuario si el correo y la contraseña coinciden; si no, null. */
    fun autenticar(correo: String, clave: String): Usuario? {
        val fila = consultas.buscarPorCorreo(correo).executeAsOneOrNull() ?: return null
        return if (fila.claveHash == hash(correo, clave)) fila.aDominio() else null
    }

    fun buscarPorId(id: Long): Usuario? =
        consultas.buscarPorId(id).executeAsOneOrNull()?.aDominio()

    private fun hash(correo: String, clave: String): String = sha256("$correo:$clave")
}

private fun UsuarioFila.aDominio() = Usuario(id = id, nombre = nombre, correo = correo)
