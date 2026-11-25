package edu.unicauca.unipass

import android.app.Application
import edu.unicauca.unipass.datos.AndroidDriverFactory

/**
 * Crea el contenedor UNA sola vez por proceso, así el estado (sesión, ofertas, navegación)
 * sobrevive cuando Android recrea la Activity (por ejemplo al rotar la pantalla).
 */
class UniPassApplication : Application() {
    val contenedor: AppContainer by lazy { AppContainer(AndroidDriverFactory(this)) }
}
