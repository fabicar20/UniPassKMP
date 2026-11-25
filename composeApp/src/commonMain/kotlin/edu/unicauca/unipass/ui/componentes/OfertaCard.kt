package edu.unicauca.unipass.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.dominio.Oferta

/**
 * Tarjeta reutilizable de una oferta. El parámetro `pie` permite poner
 * contenido distinto al final en cada pantalla.
 */
@Composable
fun OfertaCard(
    oferta: Oferta,
    alClic: () -> Unit,
    modifier: Modifier = Modifier,
    pie: @Composable ColumnScope.() -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = alClic)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(oferta.titulo, style = MaterialTheme.typography.titleMedium)
            Text(oferta.empresa, style = MaterialTheme.typography.bodyMedium)
            Text(
                "📍 ${oferta.ubicacion}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                "${oferta.tipo} · ${oferta.modalidad}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (oferta.vieneDeApi) {
                Text(
                    "🌐 Oferta en línea · ${oferta.origen}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            oferta.textoPublicacion?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            pie()
        }
    }
}
