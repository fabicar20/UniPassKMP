package edu.unicauca.unipass.datos

import edu.unicauca.unipass.db.Oferta as OfertaFila
import edu.unicauca.unipass.db.UniPassDatabase
import edu.unicauca.unipass.dominio.DatosIniciales
import edu.unicauca.unipass.dominio.InteraccionOferta
import edu.unicauca.unipass.dominio.Oferta
import edu.unicauca.unipass.red.OfertaApiService
import edu.unicauca.unipass.red.aOferta

/** Resultado de pedir ofertas a la API. */
data class ResultadoSincronizacion(val recibidas: Int, val nuevas: Int)

/**
 * Une la base de datos local (SQLDelight) con la API en línea (Ktor).
 * Los ViewModels solo hablan con este repositorio.
 */
class OfertaRepository(
    private val baseDeDatos: UniPassDatabase,
    private val api: OfertaApiService,
) {
    private val ofertas = baseDeDatos.ofertaQueries
    private val interacciones = baseDeDatos.ofertaUsuarioQueries

    init {
        sembrarSiVacia()
    }

    // ---------- Lectura ----------

    fun todas(): List<Oferta> = ofertas.todas().executeAsList().map { it.aDominio() }

    fun interaccionesDe(usuarioId: Long): Map<Long, InteraccionOferta> =
        interacciones.porUsuario(usuarioId).executeAsList().associate { fila ->
            fila.ofertaId to InteraccionOferta(
                ofertaId = fila.ofertaId,
                guardada = fila.guardada,
                estadoPostulacion = fila.estadoPostulacion,
            )
        }

    // ---------- Acciones del usuario ----------

    fun fijarGuardada(usuarioId: Long, ofertaId: Long, guardada: Boolean) {
        baseDeDatos.transaction {
            interacciones.asegurarFila(usuarioId, ofertaId)
            interacciones.actualizarGuardada(guardada, usuarioId, ofertaId)
        }
    }

    fun fijarPostulacion(usuarioId: Long, ofertaId: Long, estado: String) {
        baseDeDatos.transaction {
            interacciones.asegurarFila(usuarioId, ofertaId)
            interacciones.actualizarPostulacion(estado, usuarioId, ofertaId)
        }
    }

    fun eliminarPostulacion(usuarioId: Long, ofertaId: Long) {
        interacciones.eliminarPostulacion(usuarioId, ofertaId)
    }
    // ---------- Red ----------

    /** Pide ofertas a la API y las guarda en la base de datos (las que ya existen se ignoran). */
    suspend fun sincronizarDesdeApi(limite: Int = 20): ResultadoSincronizacion {
        val trabajos = api.obtenerOfertas(limite)
        val antes = ofertas.contar().executeAsOne()

        baseDeDatos.transaction {
            for (trabajo in trabajos) {
                insertar(trabajo.aOferta())
            }
        }

        val despues = ofertas.contar().executeAsOne()
        return ResultadoSincronizacion(recibidas = trabajos.size, nuevas = (despues - antes).toInt())
    }

    // ---------- Privado ----------

    private fun sembrarSiVacia() {
        if (ofertas.contar().executeAsOne() == 0L) {
            baseDeDatos.transaction {
                for (oferta in DatosIniciales.ofertas) insertar(oferta)
            }
        }
    }

    private fun insertar(o: Oferta) {
        ofertas.insertarOIgnorar(
            id = o.id,
            titulo = o.titulo,
            empresa = o.empresa,
            ubicacion = o.ubicacion,
            modalidad = o.modalidad,
            tipo = o.tipo,
            remuneracion = o.remuneracion,
            duracion = o.duracion,
            horario = o.horario,
            responsabilidades = o.responsabilidades,
            requisitos = o.requisitos,
            validaRequisitoGrado = o.validaRequisitoGrado,
            descripcion = o.descripcion,
            urlOriginal = o.urlOriginal,
            origen = o.origen,
            fechaPublicacion = o.fechaPublicacion,
        )
    }
}

private fun OfertaFila.aDominio() = Oferta(
    id = id,
    titulo = titulo,
    empresa = empresa,
    ubicacion = ubicacion,
    modalidad = modalidad,
    tipo = tipo,
    remuneracion = remuneracion,
    duracion = duracion,
    horario = horario,
    responsabilidades = responsabilidades,
    requisitos = requisitos,
    validaRequisitoGrado = validaRequisitoGrado,
    descripcion = descripcion,
    urlOriginal = urlOriginal,
    origen = origen,
    fechaPublicacion = fechaPublicacion,
)
