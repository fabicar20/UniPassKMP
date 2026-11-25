package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.dominio.Oferta
import edu.unicauca.unipass.presentacion.OfertasViewModel
import edu.unicauca.unipass.ui.componentes.BarraSuperior
import edu.unicauca.unipass.ui.componentes.EstadoVacio
import edu.unicauca.unipass.ui.componentes.EtiquetaChip
import edu.unicauca.unipass.ui.componentes.TarjetaSeccion

@Composable
fun DetalleScreen(
    ofertaId: Long,
    ofertas: OfertasViewModel,
    alVolver: () -> Unit,
) {
    val estado by ofertas.estado.collectAsState()
    val oferta = estado.oferta(ofertaId)
    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = { BarraSuperior("Detalle", alVolver) }) { relleno ->
        if (oferta == null) {
            EstadoVacio("Esta oferta ya no está disponible.", Modifier.padding(relleno))
        } else {
            val estadoPostulacion = estado.estadoPostulacion(oferta)
            val guardada = estado.estaGuardada(oferta)

            Column(
                modifier = Modifier
                    .padding(relleno)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(oferta.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(oferta.empresa, style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EtiquetaChip(oferta.tipo)
                    EtiquetaChip(oferta.modalidad)
                    if (oferta.vieneDeApi) EtiquetaChip(oferta.origen, resaltada = true)
                }

                TarjetaSeccion(
                    titulo = if (oferta.validaRequisitoGrado) "✓ Válida para requisito de grado"
                    else "No válida para requisito de grado",
                ) {
                    Dato(" Ubicación", oferta.ubicacion)
                    Dato("Duración", oferta.duracion)
                    Dato(" Horario", oferta.horario)
                    Dato(" Remuneración", oferta.remuneracion)
                    oferta.fechaPublicacion?.let { Dato(" Publicación", it) }
                }

                if (oferta.listaResponsabilidades.isNotEmpty()) {
                    SeccionLista("Responsabilidades", oferta.listaResponsabilidades)
                }
                if (oferta.listaRequisitos.isNotEmpty()) {
                    SeccionLista("Requisitos", oferta.listaRequisitos)
                }
                if (oferta.descripcion.isNotEmpty()) {
                    Text("Descripción", style = MaterialTheme.typography.titleLarge)
                    Text(oferta.descripcion, style = MaterialTheme.typography.bodyMedium)
                }

                if (oferta.urlOriginal.isNotEmpty()) {
                    TextButton(onClick = { uriHandler.openUri(oferta.urlOriginal) }) {
                        Text("Ver oferta original ↗")
                    }
                }

                Button(
                    onClick = { ofertas.postular(oferta) },
                    enabled = estadoPostulacion == null,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text(
                        if (estadoPostulacion != null) "✓ Ya te postulaste ($estadoPostulacion)"
                        else "Postularme ahora"
                    )
                }

                OutlinedButton(
                    onClick = { ofertas.alternarGuardada(oferta) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text(if (guardada) "Quitar de guardados" else "Guardar oferta")
                }
            }
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String) {
    Column {
        Text(titulo, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Text(
            valor.ifEmpty { "No especificado" },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SeccionLista(titulo: String, elementos: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleLarge)
        elementos.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
    }
}
