package edu.unicauca.unipass.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.presentacion.OfertasUiState
import edu.unicauca.unipass.presentacion.OfertasViewModel

/** Barra superior con flecha de volver (opcional). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(titulo: String, alVolver: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            if (alVolver != null) {
                IconButton(onClick = alVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}

/** Mensaje con ícono cuando una lista está vacía. */
@Composable
fun EstadoVacio(mensaje: String, modifier: Modifier = Modifier) {
    Text(
        text = mensaje,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth().padding(vertical = 32.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
    )
}

/** Campo de búsqueda con lupa y botón para borrar. */
@Composable
fun BarraBusqueda(texto: String, alCambiar: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = texto,
        onValueChange = alCambiar,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (texto.isNotEmpty()) {
                IconButton(onClick = { alCambiar("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Borrar búsqueda")
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    )
}

/** Fila de texto con un Switch a la derecha. */
@Composable
fun FilaConSwitch(texto: String, activado: Boolean, alCambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(texto, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Switch(checked = activado, onCheckedChange = alCambiar)
    }
}

/**
 * Filtros compartidos por Inicio y Buscar (mismo componente = mismos filtros).
 * Se combinan entre sí y con el texto de búsqueda.
 */
@Composable
fun FiltrosOferta(estado: OfertasUiState, vm: OfertasViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FiltroChipMenu("Ubicación", estado.ubicacion, listOf("Popayán, Cauca", "Remoto"), vm::cambiarUbicacion)
            FiltroChipMenu("Modalidad", estado.modalidad, listOf("Presencial", "Remoto", "Híbrido"), vm::cambiarModalidad)
            FiltroChipMenu("Tipo", estado.tipo, listOf("Pasantía", "Empleo"), vm::cambiarTipo)
        }
        FilaConSwitch(
            texto = "Solo ofertas válidas para requisito de grado",
            activado = estado.soloRequisitoGrado,
            alCambiar = vm::cambiarSoloRequisitoGrado,
        )
        FilaConSwitch(
            texto = "Mostrar primero las más recientes",
            activado = estado.masRecientesPrimero,
            alCambiar = vm::cambiarMasRecientesPrimero,
        )
    }
}

/** Tarjeta con un título y contenido (se usa en "Acerca de" y en el perfil). */
@Composable
fun TarjetaSeccion(
    titulo: String,
    modifier: Modifier = Modifier,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}
