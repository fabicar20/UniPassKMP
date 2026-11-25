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
import edu.unicauca.unipass.ui.componentes.EstadoVacio
import edu.unicauca.unipass.ui.componentes.OfertaCard

@Composable
fun GuardadosScreen(
    ofertas: OfertasViewModel,
    alAbrirDetalle: (Long) -> Unit,
) {
    val estado by ofertas.estado.collectAsState()
    val lista = estado.guardadas

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text("Ofertas guardadas", style = MaterialTheme.typography.headlineMedium) }
        item {
            Text(
                "Aquí encontrarás las oportunidades que guardaste.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (lista.isEmpty()) {
            item {
                EstadoVacio("Aún no has guardado ninguna oferta.\nAbre una oferta y toca \"Guardar oferta\".")
            }
        }
        items(lista, key = { it.id }) { oferta ->
            OfertaCard(oferta = oferta, alClic = { alAbrirDetalle(oferta.id) }) {
                TextButton(onClick = { ofertas.alternarGuardada(oferta) }) {
                    Text("Quitar de guardados")
                }
            }
        }
    }
}
