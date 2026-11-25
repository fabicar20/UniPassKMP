package edu.unicauca.unipass.presentacion

import edu.unicauca.unipass.datos.ClavesPreferencia
import edu.unicauca.unipass.datos.PreferenciaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Ajustes de la app. El modo oscuro se guarda y se recuerda entre sesiones. */
class AjustesViewModel(private val preferencias: PreferenciaRepository) {

    private val _modoOscuro = MutableStateFlow(
        preferencias.obtener(ClavesPreferencia.MODO_OSCURO) == "true"
    )
    val modoOscuro: StateFlow<Boolean> = _modoOscuro.asStateFlow()

    fun cambiarModoOscuro(activado: Boolean) {
        preferencias.guardar(ClavesPreferencia.MODO_OSCURO, activado.toString())
        _modoOscuro.value = activado
    }
}
