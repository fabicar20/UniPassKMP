package edu.unicauca.unipass.red

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

/** Error de red con un mensaje listo para mostrarle al usuario. */
class ErrorApi(mensaje: String) : Exception(mensaje)

/**
 * Capa de RED: pide las ofertas a una API pública en Internet con Ktor
 * y convierte el JSON a objetos Kotlin con kotlinx.serialization.
 *
 * API usada: Remotive (https://remotive.com/api/remote-jobs) - pública, sin llave.
 * Ktor usa OkHttp como motor en Android y en Desktop.
 */
class OfertaApiService(
    private val cliente: HttpClient = HttpClient(),
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun obtenerOfertas(limite: Int = 20): List<RemotiveJob> {
        try {
            val respuesta = cliente.get("https://remotive.com/api/remote-jobs") {
                parameter("category", "software-dev")
                parameter("limit", limite)
                header("Accept", "application/json")
            }
            if (!respuesta.status.isSuccess()) {
                throw ErrorApi("El servicio respondió con un error (código ${respuesta.status.value}).")
            }
            return json.decodeFromString<RemotiveRespuesta>(respuesta.bodyAsText()).jobs
        } catch (e: CancellationException) {
            throw e
        } catch (e: ErrorApi) {
            throw e
        } catch (e: SerializationException) {
            throw ErrorApi("No se pudo leer la respuesta del servicio.")
        } catch (e: Exception) {
            throw ErrorApi("No se pudo conectar con el servicio. Revisa tu conexión a Internet.")
        }
    }
}
