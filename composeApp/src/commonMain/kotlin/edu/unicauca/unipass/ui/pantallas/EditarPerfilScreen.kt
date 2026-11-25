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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.presentacion.PerfilViewModel
import edu.unicauca.unipass.ui.componentes.BarraSuperior

@Composable
fun EditarPerfilScreen(
    perfil: PerfilViewModel,
    alVolver: () -> Unit,
) {
    val actual by perfil.perfil.collectAsState()

    // Estado propio del formulario, con los valores guardados como punto de partida.
    var universidad by rememberSaveable { mutableStateOf(actual?.universidad ?: "") }
    var carrera by rememberSaveable { mutableStateOf(actual?.carrera ?: "") }
    var semestre by rememberSaveable { mutableStateOf(actual?.semestre?.toIntOrNull() ?: 0) }  // 0 = sin indicar
    var habilidades by rememberSaveable { mutableStateOf(actual?.habilidades ?: "") }
    var idiomas by rememberSaveable { mutableStateOf(actual?.idiomas ?: "") }
    var sobreMi by rememberSaveable { mutableStateOf(actual?.sobreMi ?: "") }

    Scaffold(topBar = { BarraSuperior("Editar perfil", alVolver) }) { relleno ->
        Column(
            modifier = Modifier
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                value = universidad,
                onValueChange = { universidad = it },
                label = { Text("Universidad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = carrera,
                onValueChange = { carrera = it },
                label = { Text("Carrera") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Contador de semestre (equivale al Stepper de SwiftUI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    if (semestre == 0) "Semestre: sin indicar" else "Semestre: $semestre",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(onClick = { if (semestre > 0) semestre-- }) { Text("−") }
                OutlinedButton(onClick = { if (semestre < 12) semestre++ }) { Text("+") }
            }

            OutlinedTextField(
                value = habilidades,
                onValueChange = { habilidades = it },
                label = { Text("Habilidades (separadas por comas)") },
                placeholder = { Text("Kotlin, Redes, Excel") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = idiomas,
                onValueChange = { idiomas = it },
                label = { Text("Idiomas (uno por línea)") },
                placeholder = { Text("Español — Nativo") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = sobreMi,
                onValueChange = { sobreMi = it },
                label = { Text("Sobre mí") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    perfil.guardar(
                        universidad = universidad,
                        carrera = carrera,
                        semestre = if (semestre == 0) "" else semestre.toString(),
                        habilidades = habilidades,
                        idiomas = idiomas,
                        sobreMi = sobreMi,
                    )
                    alVolver()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Guardar")
            }
            OutlinedButton(
                onClick = alVolver,
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Cancelar")
            }
        }
    }
}
