package edu.unicauca.unipass.dominio

/** Las ofertas de la API usan ID_BASE_API + id remoto para no chocar con las locales (1, 2, 3...). */
const val ID_BASE_API = 1_000_000L

/** Oferta de pasantía o empleo. */
data class Oferta(
    val id: Long,
    val titulo: String,
    val empresa: String,
    val ubicacion: String,
    val modalidad: String,          // Presencial, Remoto, Híbrido
    val tipo: String,               // Pasantía, Empleo
    val remuneracion: String,
    val duracion: String = "",
    val horario: String = "",
    val responsabilidades: String = "",   // una por línea
    val requisitos: String = "",          // uno por línea
    val validaRequisitoGrado: Boolean = true,
    val descripcion: String = "",
    val urlOriginal: String = "",
    val origen: String = "Local",         // "Local" o "Remotive"
    val fechaPublicacion: String? = null, // "2026-11-06" (solo ofertas de la API)
) {
    val listaResponsabilidades: List<String> get() = lineas(responsabilidades)
    val listaRequisitos: List<String> get() = lineas(requisitos)
    val vieneDeApi: Boolean get() = origen != "Local"
    val textoPublicacion: String? get() = fechaPublicacion?.let { "Publicada el $it" }
}

private fun lineas(texto: String): List<String> =
    texto.lines().map { it.trim() }.filter { it.isNotEmpty() }

/** Usuario registrado (la contraseña nunca sale de la capa de datos). */
data class Usuario(val id: Long, val nombre: String, val correo: String)

/** Datos de quien tiene la sesión abierta. */
data class Sesion(val id: Long, val nombre: String, val correo: String)

/** Datos del perfil del usuario. */
data class PerfilUsuario(
    val usuarioId: Long,
    val universidad: String = "",
    val carrera: String = "",
    val semestre: String = "",
    val habilidades: String = "",   // separadas por comas
    val idiomas: String = "",       // uno por línea
    val sobreMi: String = "",
) {
    /** Porcentaje de campos que el usuario ya llenó. */
    val porcentajeCompletado: Int
        get() {
            val campos = listOf(universidad, carrera, semestre, habilidades, idiomas, sobreMi)
            return campos.count { it.isNotBlank() } * 100 / campos.size
        }

    val listaHabilidades: List<String>
        get() = habilidades.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val listaIdiomas: List<String>
        get() = idiomas.lines().map { it.trim() }.filter { it.isNotEmpty() }
}

/** Lo que el usuario actual hizo con una oferta. */
data class InteraccionOferta(
    val ofertaId: Long,
    val guardada: Boolean,
    val estadoPostulacion: String?,
)
