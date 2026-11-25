package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.dominio.esCorreoValido
import edu.unicauca.unipass.presentacion.AuthViewModel
import edu.unicauca.unipass.ui.componentes.BarraSuperior

@Composable
fun RegistroScreen(auth: AuthViewModel, alVolver: () -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmacion by rememberSaveable { mutableStateOf("") }
    var errorLocal by rememberSaveable { mutableStateOf<String?>(null) }
    val errorAuth by auth.error.collectAsState()

    DisposableEffect(Unit) {
        auth.limpiarError()
        onDispose { }
    }

    Scaffold(
        topBar = { BarraSuperior("Crear cuenta", alVolver) },
    ) { relleno ->
        Column(
            modifier = Modifier
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                "Empieza a buscar tu pasantía o práctica profesional",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo institucional") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña (mínimo 6 caracteres)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = confirmacion,
                onValueChange = { confirmacion = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            val mensaje = errorLocal ?: errorAuth
            if (mensaje != null) {
                Text(mensaje, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> errorLocal = "Escribe tu nombre."
                        !correo.trim().esCorreoValido() -> errorLocal = "Escribe un correo válido."
                        clave.length < 6 -> errorLocal = "La contraseña debe tener mínimo 6 caracteres."
                        clave != confirmacion -> errorLocal = "Las contraseñas no coinciden."
                        else -> {
                            errorLocal = null
                            // Si todo sale bien, se abre la sesión y el contenedor navega a Inicio.
                            auth.registrar(nombre, correo, clave)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Registrarme")
            }

            OutlinedButton(
                onClick = {
                    auth.limpiarError()
                    alVolver()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Ya tengo cuenta")
            }
        }
    }
}
