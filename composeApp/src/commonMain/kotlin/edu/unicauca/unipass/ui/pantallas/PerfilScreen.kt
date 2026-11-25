package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.presentacion.AjustesViewModel
import edu.unicauca.unipass.presentacion.AuthViewModel
import edu.unicauca.unipass.presentacion.PerfilViewModel
import edu.unicauca.unipass.ui.componentes.EtiquetaChip
import edu.unicauca.unipass.ui.componentes.FilaConSwitch
import edu.unicauca.unipass.ui.componentes.TarjetaSeccion

@Composable
fun PerfilScreen(
    auth: AuthViewModel,
    perfil: PerfilViewModel,
    ajustes: AjustesViewModel,
    alEditar: () -> Unit,
    alAbrirAcerca: () -> Unit,
) {
    val sesion by auth.sesion.collectAsState()
    val datos by perfil.perfil.collectAsState()
    val modoOscuro by ajustes.modoOscuro.collectAsState()
    val porcentaje = datos?.porcentajeCompletado ?: 0

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)

        // ---------- Datos principales ----------
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                sesion?.nombre?.ifEmpty { "Estudiante" } ?: "Estudiante",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            sesion?.correo?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            datos?.universidad?.takeIf { it.isNotEmpty() }?.let { Text(it) }
            datos?.carrera?.takeIf { it.isNotEmpty() }?.let { Text(it) }
            datos?.semestre?.takeIf { it.isNotEmpty() }?.let { Text("Semestre $it") }
        }

        // ---------- Perfil completado ----------
        TarjetaSeccion("Perfil completado") {
            LinearProgressIndicator(
                progress = { porcentaje / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text("$porcentaje% completado", color = MaterialTheme.colorScheme.primary)
            Text(
                "Completa tu información para encontrar oportunidades más compatibles.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = alEditar,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(48.dp),
            ) {
                Text(if (porcentaje == 0) "Completar perfil" else "Editar perfil")
            }
        }

        // ---------- Sobre mí ----------
        datos?.sobreMi?.takeIf { it.isNotEmpty() }?.let {
            Text("Sobre mí", style = MaterialTheme.typography.titleLarge)
            Text(it)
        }

        // ---------- Habilidades ----------
        Text("Habilidades", style = MaterialTheme.typography.titleLarge)
        val habilidades = datos?.listaHabilidades.orEmpty()
        if (habilidades.isEmpty()) {
            Text("Aún no has agregado habilidades.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                habilidades.forEach { EtiquetaChip(it) }
            }
        }

        // ---------- Idiomas ----------
        Text("Idiomas", style = MaterialTheme.typography.titleLarge)
        val idiomas = datos?.listaIdiomas.orEmpty()
        if (idiomas.isEmpty()) {
            Text("Aún no has agregado idiomas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            idiomas.forEach { Text(it) }
        }

        // ---------- Ajustes ----------
        Text("Ajustes", style = MaterialTheme.typography.titleLarge)
        FilaConSwitch(
            texto = "Modo oscuro (se recuerda al cerrar la app)",
            activado = modoOscuro,
            alCambiar = ajustes::cambiarModoOscuro,
        )

        // ---------- Botones ----------
        OutlinedButton(
            onClick = alAbrirAcerca,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(52.dp),
        ) {
            Text("Acerca de UniPass y créditos")
        }
        Button(
            onClick = auth::cerrarSesion,
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text("Cerrar sesión")
        }
    }
}
