package edu.unicauca.unipass.presentacion

import edu.unicauca.unipass.datos.PerfilRepository
import edu.unicauca.unipass.dominio.PerfilUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Carga y guarda el perfil del usuario que tiene la sesión abierta. */
class PerfilViewModel(private val repositorio: PerfilRepository) {

    private val _perfil = MutableStateFlow<PerfilUsuario?>(null)
    val perfil: StateFlow<PerfilUsuario?> = _perfil.asStateFlow()

    private var usuarioId: Long? = null

    fun fijarUsuario(id: Long?) {
        usuarioId = id
        _perfil.value = if (id == null) null else repositorio.obtener(id)
    }

    fun guardar(
        universidad: String,
        carrera: String,
        semestre: String,
        habilidades: String,
        idiomas: String,
        sobreMi: String,
    ) {
        val uid = usuarioId ?: return
        val nuevo = PerfilUsuario(
            usuarioId = uid,
            universidad = universidad.trim(),
            carrera = carrera.trim(),
            semestre = semestre.trim(),
            habilidades = habilidades.trim(),
            idiomas = idiomas.trim(),
            sobreMi = sobreMi.trim(),
        )
        repositorio.guardar(nuevo)
        _perfil.value = nuevo
    }
}
