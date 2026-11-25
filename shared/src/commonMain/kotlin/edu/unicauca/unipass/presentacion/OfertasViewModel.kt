package edu.unicauca.unipass.presentacion

import edu.unicauca.unipass.datos.OfertaRepository
import edu.unicauca.unipass.dominio.InteraccionOferta
import edu.unicauca.unipass.dominio.Oferta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Todo lo que la pantalla necesita saber sobre las ofertas (un solo objeto inmutable). */
data class OfertasUiState(
    val ofertas: List<Oferta> = emptyList(),
    val interacciones: Map<Long, InteraccionOferta> = emptyMap(),
    // Búsqueda y filtros (se combinan entre sí)
    val consulta: String = "",
    val ubicacion: String? = null,
    val modalidad: String? = null,
    val tipo: String? = null,
    val soloRequisitoGrado: Boolean = false,
    val masRecientesPrimero: Boolean = false,
    // Estado de la red
    val cargandoApi: Boolean = false,
    val mensajeApi: String? = null,
) {
    val ofertasFiltradas: List<Oferta>
        get() {
            val texto = consulta.trim()
            val resultado = ofertas.filter { oferta ->
                val coincideTexto = texto.isEmpty() ||
                    oferta.titulo.contains(texto, ignoreCase = true) ||
                    oferta.empresa.contains(texto, ignoreCase = true) ||
                    oferta.ubicacion.contains(texto, ignoreCase = true)
                coincideTexto &&
                    (ubicacion == null || oferta.ubicacion == ubicacion) &&
                    (modalidad == null || oferta.modalidad == modalidad) &&
                    (tipo == null || oferta.tipo == tipo) &&
                    (!soloRequisitoGrado || oferta.validaRequisitoGrado)
            }
            // Las ofertas sin fecha (las locales) quedan al final.
            return if (masRecientesPrimero) {
                resultado.sortedByDescending { it.fechaPublicacion ?: "" }
            } else {
                resultado
            }
        }

    val guardadas: List<Oferta> get() = ofertas.filter { estaGuardada(it) }
    val postuladas: List<Oferta> get() = ofertas.filter { estadoPostulacion(it) != null }
    val cantidadEnLinea: Int get() = ofertas.count { it.vieneDeApi }

    val hayFiltrosActivos: Boolean
        get() = consulta.isNotBlank() || ubicacion != null || modalidad != null ||
            tipo != null || soloRequisitoGrado || masRecientesPrimero

    fun oferta(id: Long): Oferta? = ofertas.firstOrNull { it.id == id }
    fun estaGuardada(oferta: Oferta): Boolean = interacciones[oferta.id]?.guardada == true
    fun estadoPostulacion(oferta: Oferta): String? = interacciones[oferta.id]?.estadoPostulacion
}

/**
 * ViewModel de las ofertas (MVVM). Une las tres capas:
 *  - Persistencia: lee y escribe en SQLDelight (ofertas, guardadas, postulaciones).
 *  - Red: pide ofertas nuevas a la API con Ktor (a través del repositorio).
 *  - Estado de la UI: búsqueda, filtros, cargando, mensajes.
 */
class OfertasViewModel(
    private val repositorio: OfertaRepository,
    private val scope: CoroutineScope,
) {
    private val _estado = MutableStateFlow(OfertasUiState())
    val estado: StateFlow<OfertasUiState> = _estado.asStateFlow()

    private var usuarioId: Long? = null
    private var intentoAutomatico = false

    init {
        recargar()
    }

    // ---------- Usuario ----------

    fun fijarUsuario(id: Long?) {
        usuarioId = id
        if (id == null) limpiarFiltros()
        recargar()
    }

    // ---------- Filtros ----------

    fun cambiarConsulta(texto: String) = _estado.update { it.copy(consulta = texto) }
    fun cambiarUbicacion(valor: String?) = _estado.update { it.copy(ubicacion = valor) }
    fun cambiarModalidad(valor: String?) = _estado.update { it.copy(modalidad = valor) }
    fun cambiarTipo(valor: String?) = _estado.update { it.copy(tipo = valor) }
    fun cambiarSoloRequisitoGrado(valor: Boolean) = _estado.update { it.copy(soloRequisitoGrado = valor) }
    fun cambiarMasRecientesPrimero(valor: Boolean) = _estado.update { it.copy(masRecientesPrimero = valor) }

    fun limpiarFiltros() = _estado.update {
        it.copy(
            consulta = "", ubicacion = null, modalidad = null, tipo = null,
            soloRequisitoGrado = false, masRecientesPrimero = false,
        )
    }

    // ---------- Acciones del usuario ----------

    fun alternarGuardada(oferta: Oferta) {
        val uid = usuarioId ?: return
        val nuevoValor = !_estado.value.estaGuardada(oferta)
        repositorio.fijarGuardada(uid, oferta.id, nuevoValor)
        recargar()
    }

    fun postular(oferta: Oferta) {
        val uid = usuarioId ?: return
        repositorio.fijarPostulacion(uid, oferta.id, "Postulación enviada")
        recargar()
    }

    fun actualizarEstadoPostulacion(
        oferta: Oferta,
        nuevoEstado: String
    ) {
        val uid = usuarioId ?: return

        repositorio.fijarPostulacion(
            uid,
            oferta.id,
            nuevoEstado
        )

        recargar()
    }
    fun eliminarPostulacion(oferta: Oferta) {
        val uid = usuarioId ?: return

        repositorio.eliminarPostulacion(uid, oferta.id)

        recargar()
    }
    // ---------- Red ----------

    /** Primera carga automática: solo si todavía no hay ofertas de la API guardadas. */
    fun sincronizarSiHaceFalta() {
        if (!intentoAutomatico && _estado.value.cantidadEnLinea == 0) {
            intentoAutomatico = true
            sincronizarDesdeApi()
        }
    }

    fun sincronizarDesdeApi() {
        if (_estado.value.cargandoApi) return
        _estado.update { it.copy(cargandoApi = true, mensajeApi = null) }

        scope.launch {
            val mensaje = try {
                val r = repositorio.sincronizarDesdeApi(limite = 20)
                "Se recibieron ${r.recibidas} ofertas de Remotive (${r.nuevas} nuevas)."
            } catch (e: Exception) {
                e.message ?: "No se pudieron obtener las ofertas."
            }
            recargar()
            _estado.update { it.copy(cargandoApi = false, mensajeApi = mensaje) }
        }
    }

    // ---------- Privado ----------

    private fun recargar() {
        val uid = usuarioId
        val ofertas = repositorio.todas()
        val interacciones = if (uid == null) emptyMap() else repositorio.interaccionesDe(uid)
        _estado.update { it.copy(ofertas = ofertas, interacciones = interacciones) }
    }
}
