package edu.unicauca.unipass.presentacion

import edu.unicauca.unipass.datos.ClavesPreferencia
import edu.unicauca.unipass.datos.PreferenciaRepository
import edu.unicauca.unipass.datos.UsuarioRepository
import edu.unicauca.unipass.dominio.Sesion
import edu.unicauca.unipass.dominio.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Registro, inicio y cierre de sesión. La sesión se guarda para que sobreviva al cerrar la app. */
class AuthViewModel(
    private val usuarios: UsuarioRepository,
    private val preferencias: PreferenciaRepository,
) {
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Si la sesión quedó abierta la última vez, se restaura.
        val idGuardado = preferencias.obtener(ClavesPreferencia.SESION)?.toLongOrNull()
        if (idGuardado != null) {
            _sesion.value = usuarios.buscarPorId(idGuardado)?.aSesion()
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun iniciarSesion(correo: String, clave: String): Boolean {
        _error.value = null
        val usuario = usuarios.autenticar(correo.trim().lowercase(), clave)
        if (usuario == null) {
            _error.value = "Correo o contraseña incorrectos."
            return false
        }
        abrirSesion(usuario)
        return true
    }

    fun registrar(nombre: String, correo: String, clave: String): Boolean {
        _error.value = null
        val usuario = usuarios.registrar(nombre.trim(), correo.trim().lowercase(), clave)
        if (usuario == null) {
            _error.value = "Ya existe una cuenta con ese correo."
            return false
        }
        abrirSesion(usuario)
        return true
    }

    fun cerrarSesion() {
        preferencias.borrar(ClavesPreferencia.SESION)
        _error.value = null
        _sesion.value = null
    }

    private fun abrirSesion(usuario: Usuario) {
        preferencias.guardar(ClavesPreferencia.SESION, usuario.id.toString())
        _sesion.value = usuario.aSesion()
    }
}

private fun Usuario.aSesion() = Sesion(id = id, nombre = nombre, correo = correo)
