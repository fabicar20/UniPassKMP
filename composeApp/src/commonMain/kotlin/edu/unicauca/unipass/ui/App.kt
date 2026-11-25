package edu.unicauca.unipass.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.AppContainer
import edu.unicauca.unipass.navegacion.Destino
import edu.unicauca.unipass.navegacion.esPestana
import edu.unicauca.unipass.navegacion.permiteAtras
import edu.unicauca.unipass.ui.pantallas.AcercaScreen
import edu.unicauca.unipass.ui.pantallas.BusquedaScreen
import edu.unicauca.unipass.ui.pantallas.DetalleScreen
import edu.unicauca.unipass.ui.pantallas.EditarPerfilScreen
import edu.unicauca.unipass.ui.pantallas.GuardadosScreen
import edu.unicauca.unipass.ui.pantallas.InicioScreen
import edu.unicauca.unipass.ui.pantallas.LoginScreen
import edu.unicauca.unipass.ui.pantallas.PerfilScreen
import edu.unicauca.unipass.ui.pantallas.PostulacionesScreen
import edu.unicauca.unipass.ui.pantallas.RegistroScreen
import edu.unicauca.unipass.ui.tema.UniPassTheme

/** Una pestaña de la barra inferior (o lateral en pantallas anchas). */
private class Pestana(val destino: Destino, val etiqueta: String, val icono: ImageVector)

private val PESTANAS_UI = listOf(
    Pestana(Destino.Inicio, "Inicio", Icons.Filled.Home),
    Pestana(Destino.Buscar, "Buscar", Icons.Filled.Search),
    Pestana(Destino.Postulaciones, "Postulaciones", Icons.AutoMirrored.Filled.List),
    Pestana(Destino.Guardados, "Guardados", Icons.Filled.Favorite),
    Pestana(Destino.Perfil, "Perfil", Icons.Filled.Person),
)

/**
 * Raíz de la interfaz. Es la MISMA en Android y en Desktop.
 * No decide a dónde navegar: solo dibuja el destino que le indica el `Navegador` (capa compartida).
 */
@Composable
fun App(contenedor: AppContainer) {
    val modoOscuro by contenedor.ajustes.modoOscuro.collectAsState()
    val pila by contenedor.navegador.pila.collectAsState()
    val destino = pila.last()

    UniPassTheme(modoOscuro = modoOscuro) {
        // En Android: el botón "atrás" vuelve a la pantalla anterior o, desde otra pestaña, a Inicio.
        ManejadorAtras(habilitado = pila.permiteAtras) { contenedor.navegador.atras() }

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (destino.esPestana) {
                EstructuraConPestanas(contenedor, destino)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Box(Modifier.widthIn(max = 800.dp)) {
                        ContenidoDestino(contenedor, destino)
                    }
                }
            }
        }
    }
}

/** Pantallas con pestañas: barra inferior en pantallas angostas, barra lateral en anchas. */
@Composable
private fun EstructuraConPestanas(contenedor: AppContainer, destino: Destino) {
    val navegador = contenedor.navegador

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val pantallaAncha = maxWidth >= 720.dp

        if (pantallaAncha) {
            Row(Modifier.fillMaxSize()) {
                NavigationRail {
                    PESTANAS_UI.forEach { pestana ->
                        NavigationRailItem(
                            selected = destino == pestana.destino,
                            onClick = { navegador.irAPestana(pestana.destino) },
                            icon = { Icon(pestana.icono, contentDescription = pestana.etiqueta) },
                            label = { Text(pestana.etiqueta) },
                        )
                    }
                }
                Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
                    Box(Modifier.widthIn(max = 800.dp)) {
                        ContenidoDestino(contenedor, destino)
                    }
                }
            }
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        PESTANAS_UI.forEach { pestana ->
                            NavigationBarItem(
                                selected = destino == pestana.destino,
                                onClick = { navegador.irAPestana(pestana.destino) },
                                icon = { Icon(pestana.icono, contentDescription = pestana.etiqueta) },
                                label = { Text(pestana.etiqueta) },
                            )
                        }
                    }
                }
            ) { relleno ->
                Box(Modifier.padding(relleno)) {
                    ContenidoDestino(contenedor, destino)
                }
            }
        }
    }
}

/** Dibuja la pantalla que corresponde a cada destino. */
@Composable
private fun ContenidoDestino(contenedor: AppContainer, destino: Destino) {
    val navegador = contenedor.navegador

    when (destino) {
        Destino.Login -> LoginScreen(
            auth = contenedor.auth,
            alIrARegistro = { navegador.irA(Destino.Registro) },
        )
        Destino.Registro -> RegistroScreen(
            auth = contenedor.auth,
            alVolver = { navegador.volver() },
        )
        Destino.Inicio -> InicioScreen(
            auth = contenedor.auth,
            ofertas = contenedor.ofertas,
            alAbrirDetalle = { navegador.irA(Destino.Detalle(it)) },
        )
        Destino.Buscar -> BusquedaScreen(
            ofertas = contenedor.ofertas,
            alAbrirDetalle = { navegador.irA(Destino.Detalle(it)) },
        )
        Destino.Postulaciones -> PostulacionesScreen(
            ofertas = contenedor.ofertas,
            alAbrirDetalle = { navegador.irA(Destino.Detalle(it)) },
        )
        Destino.Guardados -> GuardadosScreen(
            ofertas = contenedor.ofertas,
            alAbrirDetalle = { navegador.irA(Destino.Detalle(it)) },
        )
        Destino.Perfil -> PerfilScreen(
            auth = contenedor.auth,
            perfil = contenedor.perfil,
            ajustes = contenedor.ajustes,
            alEditar = { navegador.irA(Destino.EditarPerfil) },
            alAbrirAcerca = { navegador.irA(Destino.Acerca) },
        )
        is Destino.Detalle -> DetalleScreen(
            ofertaId = destino.ofertaId,
            ofertas = contenedor.ofertas,
            alVolver = { navegador.volver() },
        )
        Destino.EditarPerfil -> EditarPerfilScreen(
            perfil = contenedor.perfil,
            alVolver = { navegador.volver() },
        )
        Destino.Acerca -> AcercaScreen(
            alVolver = { navegador.volver() },
        )
    }
}
