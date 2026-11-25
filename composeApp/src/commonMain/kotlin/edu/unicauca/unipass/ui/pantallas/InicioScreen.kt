package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.presentacion.AuthViewModel
import edu.unicauca.unipass.presentacion.OfertasViewModel
import edu.unicauca.unipass.ui.componentes.BarraBusqueda
import edu.unicauca.unipass.ui.componentes.EstadoVacio
import edu.unicauca.unipass.ui.componentes.FiltrosOferta
import edu.unicauca.unipass.ui.componentes.OfertaCard

@Composable
fun InicioScreen(
    auth: AuthViewModel,
    ofertas: OfertasViewModel,
    alAbrirDetalle: (Long) -> Unit,
) {
    val estado by ofertas.estado.collectAsState()
    val sesion by auth.sesion.collectAsState()

    // Primera carga automática desde la API (solo si todavía no hay ofertas en línea).
    LaunchedEffect(Unit) { ofertas.sincronizarSiHaceFalta() }

    val primerNombre = sesion?.nombre?.trim()?.substringBefore(" ").orEmpty()
    val lista = estado.ofertasFiltradas

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column {
                Text(
                    if (primerNombre.isEmpty()) "Hola" else "Hola, $primerNombre",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Encuentra oportunidades para tu carrera",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            BarraBusqueda(
                texto = estado.consulta,
                alCambiar = ofertas::cambiarConsulta,
                placeholder = "¿Qué oportunidad estás buscando?",
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Filtros rápidos", style = MaterialTheme.typography.titleMedium)
                FiltrosOferta(estado, ofertas)
                if (estado.hayFiltrosActivos) {
                    TextButton(onClick = ofertas::limpiarFiltros) { Text("Limpiar filtros") }
                }
            }
        }

        // ---------- Ofertas desde la API ----------
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            " Ofertas en línea",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f),
                        )
                        if (estado.cargandoApi) {
                            CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                        } else {
                            OutlinedButton(onClick = ofertas::sincronizarDesdeApi) {
                                Icon(Icons.Filled.Refresh, contentDescription = null)
                                Text("  Actualizar")
                            }
                        }
                    }
                    Text(
                        if (estado.cantidadEnLinea == 0) "Aún no hay ofertas descargadas de Internet."
                        else "${estado.cantidadEnLinea} ofertas descargadas desde Remotive.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    estado.mensajeApi?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }

        // ---------- Lista ----------
        item {
            Text("Oportunidades para ti", style = MaterialTheme.typography.titleLarge)
        }

        if (lista.isEmpty()) {
            item { EstadoVacio("No se encontraron ofertas.") }
        }

        items(lista, key = { it.id }) { oferta ->
            OfertaCard(oferta = oferta, alClic = { alAbrirDetalle(oferta.id) }) {
                if (oferta.validaRequisitoGrado) {
                    Text(
                        "✓ Compatible con tu requisito de grado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
