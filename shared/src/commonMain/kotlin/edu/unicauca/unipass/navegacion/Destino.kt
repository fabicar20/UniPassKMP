package edu.unicauca.unipass.navegacion

/**
 * Todas las pantallas de la app. Viven en la capa compartida (commonMain),
 * así Android y Desktop navegan exactamente igual.
 */
sealed interface Destino {
    // Acceso
    data object Login : Destino
    data object Registro : Destino

    // Pestañas principales
    data object Inicio : Destino
    data object Buscar : Destino
    data object Postulaciones : Destino
    data object Guardados : Destino
    data object Perfil : Destino

    // Pantallas secundarias
    data class Detalle(val ofertaId: Long) : Destino
    data object EditarPerfil : Destino
    data object Acerca : Destino
}

/** Las 5 pestañas de la barra inferior (o lateral en Desktop). */
val PESTANAS: List<Destino> = listOf(
    Destino.Inicio,
    Destino.Buscar,
    Destino.Postulaciones,
    Destino.Guardados,
    Destino.Perfil,
)

val Destino.esPestana: Boolean get() = this in PESTANAS
