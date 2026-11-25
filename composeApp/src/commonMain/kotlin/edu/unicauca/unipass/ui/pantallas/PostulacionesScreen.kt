
package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.dominio.Oferta
import edu.unicauca.unipass.presentacion.OfertasViewModel
import edu.unicauca.unipass.ui.componentes.EstadoVacio
import edu.unicauca.unipass.ui.componentes.OfertaCard

@Composable
fun PostulacionesScreen(
    ofertas: OfertasViewModel,
    alAbrirDetalle: (Long) -> Unit,
) {
    val estado by ofertas.estado.collectAsState()
    val lista = estado.postuladas

    var ofertaSeleccionada by remember {
        mutableStateOf<Oferta?>(null)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                "Mis postulaciones",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                "Aquí puedes consultar el estado de tus postulaciones.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (lista.isEmpty()) {
            item {
                EstadoVacio(
                    "Aún no te has postulado a ninguna oferta.\nAbre una oferta y toca \"Postularme ahora\"."
                )
            }
        }

        items(lista, key = { it.id }) { oferta ->

            OfertaCard(
                oferta = oferta,
                alClic = { alAbrirDetalle(oferta.id) }
            ) {
                Text(
                    "Estado: ${estado.estadoPostulacion(oferta) ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                TextButton(
                    onClick = {
                        ofertaSeleccionada = oferta
                    }
                ) {
                    Text(
                        "Retirar postulación",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Ventana para confirmar la eliminación
    ofertaSeleccionada?.let { oferta ->

        AlertDialog(
            onDismissRequest = {
                ofertaSeleccionada = null
            },
            title = {
                Text("Retirar postulación")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas retirar tu postulación a ${oferta.titulo}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        ofertas.eliminarPostulacion(oferta)
                        ofertaSeleccionada = null
                    }
                ) {
                    Text(
                        "Sí, retirar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        ofertaSeleccionada = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
