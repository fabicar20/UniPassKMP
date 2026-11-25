package edu.unicauca.unipass.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/** Etiqueta redondeada (equivale a los AssistChip de Compose nativo). */
@Composable
fun EtiquetaChip(texto: String, modifier: Modifier = Modifier, resaltada: Boolean = false) {
    val forma = RoundedCornerShape(50)
    Text(
        text = texto,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .clip(forma)
            .background(
                if (resaltada) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainer
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

/**
 * Chip que abre un menú desplegable. Cuando hay una opción elegida se resalta.
 * `seleccion == null` significa "sin filtro".
 */
@Composable
fun FiltroChipMenu(
    titulo: String,
    seleccion: String?,
    opciones: List<String>,
    alSeleccionar: (String?) -> Unit,
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(50)

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(forma)
                .background(
                    if (seleccion != null) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainer
                )
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
                .clickable { abierto = true }
                .padding(start = 14.dp, top = 6.dp, bottom = 6.dp, end = 6.dp),
        ) {
            Text(text = seleccion ?: titulo, style = MaterialTheme.typography.labelLarge)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            DropdownMenuItem(
                text = { Text("Cualquiera") },
                onClick = {
                    alSeleccionar(null)
                    abierto = false
                },
            )
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(if (opcion == seleccion) "✓ $opcion" else opcion) },
                    onClick = {
                        alSeleccionar(opcion)
                        abierto = false
                    },
                )
            }
        }
    }
}
