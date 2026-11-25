package edu.unicauca.unipass

import edu.unicauca.unipass.datos.DriverFactory
import edu.unicauca.unipass.datos.OfertaRepository
import edu.unicauca.unipass.datos.PerfilRepository
import edu.unicauca.unipass.datos.PreferenciaRepository
import edu.unicauca.unipass.datos.UsuarioRepository
import edu.unicauca.unipass.db.UniPassDatabase
import edu.unicauca.unipass.navegacion.Destino
import edu.unicauca.unipass.navegacion.Navegador
import edu.unicauca.unipass.presentacion.AjustesViewModel
import edu.unicauca.unipass.presentacion.AuthViewModel
import edu.unicauca.unipass.presentacion.OfertasViewModel
import edu.unicauca.unipass.presentacion.PerfilViewModel
import edu.unicauca.unipass.red.OfertaApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Contenedor de dependencias hecho a mano: crea la base de datos, los repositorios,
 * los ViewModels y el navegador, y los conecta entre sí.
 *
 * Android y Desktop solo tienen que pasarle su `DriverFactory`; todo lo demás es común.
 */
class AppContainer(driverFactory: DriverFactory) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // ---- Persistencia ----
    private val baseDeDatos = UniPassDatabase(driverFactory.crearDriver())

    // ---- Repositorios ----
    private val preferencias = PreferenciaRepository(baseDeDatos)
    private val usuarios = UsuarioRepository(baseDeDatos)
    private val perfiles = PerfilRepository(baseDeDatos)
    private val ofertasRepositorio = OfertaRepository(baseDeDatos, OfertaApiService())

    // ---- ViewModels ----
    val auth = AuthViewModel(usuarios, preferencias)
    val ofertas = OfertasViewModel(ofertasRepositorio, scope)
    val perfil = PerfilViewModel(perfiles)
    val ajustes = AjustesViewModel(preferencias)

    // ---- Navegación (capa compartida) ----
    val navegador = Navegador(
        inicial = if (auth.sesion.value != null) Destino.Inicio else Destino.Login
    )

    init {
        // Cuando cambia la sesión: los ViewModels cambian de usuario y se navega solo.
        auth.sesion.onEach { sesion ->
            ofertas.fijarUsuario(sesion?.id)
            perfil.fijarUsuario(sesion?.id)
            navegador.reiniciarEn(if (sesion == null) Destino.Login else Destino.Inicio)
        }.launchIn(scope)
    }
}
