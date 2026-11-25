package edu.unicauca.unipass.datos

import edu.unicauca.unipass.db.Perfil as PerfilFila
import edu.unicauca.unipass.db.UniPassDatabase
import edu.unicauca.unipass.dominio.PerfilUsuario

class PerfilRepository(baseDeDatos: UniPassDatabase) {

    private val consultas = baseDeDatos.perfilQueries

    fun obtener(usuarioId: Long): PerfilUsuario? =
        consultas.porUsuario(usuarioId).executeAsOneOrNull()?.aDominio()

    fun guardar(perfil: PerfilUsuario) {
        consultas.guardar(
            usuarioId = perfil.usuarioId,
            universidad = perfil.universidad,
            carrera = perfil.carrera,
            semestre = perfil.semestre,
            habilidades = perfil.habilidades,
            idiomas = perfil.idiomas,
            sobreMi = perfil.sobreMi,
        )
    }
}

private fun PerfilFila.aDominio() = PerfilUsuario(
    usuarioId = usuarioId,
    universidad = universidad,
    carrera = carrera,
    semestre = semestre,
    habilidades = habilidades,
    idiomas = idiomas,
    sobreMi = sobreMi,
)
