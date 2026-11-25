package edu.unicauca.unipass.navegacion

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Navegación gestionada en la capa compartida de KMP.
 *
 * Mantiene una PILA de destinos. La interfaz (Compose) solo observa `pila`
 * y dibuja el último destino; no decide a dónde ir.
 */
class Navegador(inicial: Destino) {

    private val _pila = MutableStateFlow(listOf(inicial))
    val pila: StateFlow<List<Destino>> = _pila.asStateFlow()

    val actual: Destino get() = _pila.value.last()

    /** Abre una pantalla encima de la actual (ej. lista -> detalle). */
    fun irA(destino: Destino) {
        _pila.update { it + destino }
    }

    /** Cambia de pestaña: la pila queda solo con esa pestaña. */
    fun irAPestana(destino: Destino) {
        _pila.value = listOf(destino)
    }

    /** Borra todo el historial y empieza de cero (ej. al iniciar o cerrar sesión). */
    fun reiniciarEn(destino: Destino) {
        _pila.value = listOf(destino)
    }

    /** Vuelve a la pantalla anterior. Devuelve false si ya no hay a dónde volver. */
    fun volver(): Boolean {
        if (_pila.value.size <= 1) return false
        _pila.update { it.dropLast(1) }
        return true
    }

    /**
     * Botón "atrás" del sistema:
     *  1. Si hay una pantalla anterior, vuelve a ella.
     *  2. Si está en una pestaña distinta de Inicio, va a Inicio.
     *  3. Si ya está en Inicio (o en Login), devuelve false y la app se cierra.
     */
    fun atras(): Boolean {
        if (volver()) return true
        if (actual.esPestana && actual != Destino.Inicio) {
            irAPestana(Destino.Inicio)
            return true
        }
        return false
    }
}

/** true si el botón "atrás" tiene a dónde llevar al usuario con esta pila (ver [Navegador.atras]). */
val List<Destino>.permiteAtras: Boolean
    get() = size > 1 || (last().esPestana && last() != Destino.Inicio)
