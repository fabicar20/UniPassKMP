package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.presentacion.OfertasViewModel
import edu.unicauca.unipass.ui.componentes.BarraBusqueda
import edu.unicauca.unipass.ui.componentes.EstadoVacio
import edu.unicauca.unipass.ui.componentes.FiltrosOferta
import edu.unicauca.unipass.ui.componentes.OfertaCard

@Composable
fun BusquedaScreen(
    ofertas: OfertasViewModel,
    alAbrirDetalle: (Long) -> Unit,
) {
    val estado by ofertas.estado.collectAsState()
    val lista = estado.ofertasFiltradas

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Buscar oportunidades", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            BarraBusqueda(
                texto = estado.consulta,
                alCambiar = ofertas::cambiarConsulta,
                placeholder = "Buscar una oportunidad",
            )
        }
        item { Text("Filtros", style = MaterialTheme.typography.titleMedium) }
        item { FiltrosOferta(estado, ofertas) }
        item {
            Text("${lista.size} oportunidades encontradas", style = MaterialTheme.typography.titleMedium)
        }
        if (estado.hayFiltrosActivos) {
            item { TextButton(onClick = ofertas::limpiarFiltros) { Text("Limpiar filtros") } }
        }
        if (lista.isEmpty()) {
            item { EstadoVacio("No se encontraron ofertas con esos filtros.") }
        }
        items(lista, key = { it.id }) { oferta ->
            OfertaCard(oferta = oferta, alClic = { alAbrirDetalle(oferta.id) }) {
                if (oferta.validaRequisitoGrado) {
                    Text(
                        "✓ Requisito de grado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
