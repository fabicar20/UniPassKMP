package edu.unicauca.unipass.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.unicauca.unipass.plataforma.nombrePlataforma
import edu.unicauca.unipass.ui.componentes.BarraSuperior
import edu.unicauca.unipass.ui.componentes.TarjetaSeccion

private val INTEGRANTES = listOf(
    "Fabiana Zuleima Cárdenas",
    "Karol Dayana Ocoro Natez",
    "Leiner Santiago Claros",
)

/** Descripción de la aplicación y créditos con los nombres de los estudiantes. */
@Composable
fun AcercaScreen(alVolver: () -> Unit) {
    Scaffold(topBar = { BarraSuperior("Acerca de UniPass", alVolver) }) { relleno ->
        Column(
            modifier = Modifier
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TarjetaSeccion("¿Qué es UniPass?") {
                Text(
                    "UniPass es una aplicación para estudiantes universitarios que necesitan encontrar " +
                        "una pasantía o práctica profesional (requisito de grado en muchas universidades) " +
                        "y todavía no tienen experiencia laboral. Reúne las ofertas en un solo lugar, " +
                        "indica cuáles sirven como requisito de grado y permite buscar, guardar y " +
                        "postularse en pocos pasos."
                )
            }

            TarjetaSeccion("Qué puedes hacer") {
                Text("• Crear una cuenta e iniciar sesión (la sesión se recuerda)")
                Text("• Buscar ofertas y filtrarlas por ubicación, modalidad y tipo")
                Text("• Ver el detalle de cada oferta y sus requisitos")
                Text("• Guardar ofertas para verlas después")
                Text("• Postularte y seguir el estado de tus postulaciones")
                Text("• Descargar ofertas reales desde Internet")
                Text("• Cambiar entre modo claro y oscuro")
            }

            TarjetaSeccion("Tecnologías (Kotlin Multiplatform)") {
                Text("• Kotlin Multiplatform: lógica compartida (módulo :shared)")
                Text("• Compose Multiplatform: la misma interfaz en Android y Desktop")
                Text("• SQLDelight: base de datos local (persistencia)")
                Text("• Ktor + kotlinx.serialization: consumo de la API en línea")
                Text("• Corrutinas y StateFlow: estado y operaciones asíncronas")
                Text("• Navegación en la capa compartida (Navegador + Destino)")
            }

            TarjetaSeccion("Fuente de datos en línea") {
                Text(
                    "Las ofertas en línea se obtienen de la API pública de Remotive (remotive.com). " +
                        "Cada oferta enlaza a su publicación original."
                )
            }

            TarjetaSeccion("Créditos") {
                INTEGRANTES.forEach { Text(it) }
                Text(
                    "Electiva Desarrollo de Aplicaciones Móviles",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(
                    "Departamento de Telemática — Universidad del Cauca",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    "Versión 1.0 · Ejecutándose en: ${nombrePlataforma()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
