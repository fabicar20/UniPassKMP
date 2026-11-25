package edu.unicauca.unipass.datos

import edu.unicauca.unipass.db.UniPassDatabase

object ClavesPreferencia {
    const val SESION = "sesion_usuario_id"
    const val MODO_OSCURO = "modo_oscuro"
}

/** Guarda pares clave/valor en SQLDelight (equivale a SharedPreferences / DataStore). */
class PreferenciaRepository(baseDeDatos: UniPassDatabase) {

    private val consultas = baseDeDatos.preferenciaQueries

    fun obtener(clave: String): String? =
        consultas.obtener(clave).executeAsOneOrNull()

    fun guardar(clave: String, valor: String) {
        consultas.guardar(clave, valor)
    }

    fun borrar(clave: String) {
        consultas.borrar(clave)
    }
}
