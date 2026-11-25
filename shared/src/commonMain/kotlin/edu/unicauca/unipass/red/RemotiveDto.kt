package edu.unicauca.unipass.red

import edu.unicauca.unipass.dominio.ID_BASE_API
import edu.unicauca.unipass.dominio.Oferta
import edu.unicauca.unipass.dominio.TextoLimpio
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Respuesta de https://remotive.com/api/remote-jobs */
@Serializable
data class RemotiveRespuesta(
    val jobs: List<RemotiveJob> = emptyList(),
)

/** Un empleo tal como lo devuelve la API. Los campos opcionales evitan fallos si la API omite alguno. */
@Serializable
data class RemotiveJob(
    val id: Long,
    val url: String? = null,
    val title: String,
    @SerialName("company_name") val companyName: String? = null,
    val category: String? = null,
    @SerialName("job_type") val jobType: String? = null,
    @SerialName("publication_date") val publicationDate: String? = null,
    @SerialName("candidate_required_location") val candidateRequiredLocation: String? = null,
    val salary: String? = null,
    val description: String? = null,
)

/** Convierte un empleo de la API en una `Oferta` de la app. */
fun RemotiveJob.aOferta(): Oferta {
    val tipoTrabajo = (jobType ?: "").lowercase()
    val esPasantia = tipoTrabajo == "internship"

    val horario = when (tipoTrabajo) {
        "full_time" -> "Tiempo completo"
        "part_time" -> "Medio tiempo"
        "contract" -> "Por contrato"
        "freelance" -> "Freelance"
        "internship" -> "Pasantía"
        else -> "No especificado"
    }

    var requisitos = ""
    val lugar = candidateRequiredLocation?.trim().orEmpty()
    if (lugar.isNotEmpty()) requisitos = "Disponible para: $lugar"
    val categoria = category?.trim().orEmpty()
    if (categoria.isNotEmpty()) {
        requisitos += (if (requisitos.isEmpty()) "" else "\n") + "Categoría: $categoria"
    }

    val sueldo = salary?.trim().orEmpty()

    return Oferta(
        id = ID_BASE_API + id,
        titulo = title.trim(),
        empresa = (companyName ?: "Empresa no indicada").trim(),
        ubicacion = "Remoto",
        modalidad = "Remoto",
        tipo = if (esPasantia) "Pasantía" else "Empleo",
        remuneracion = sueldo.ifEmpty { "No especificada" },
        duracion = "No especificada",
        horario = horario,
        responsabilidades = "",
        requisitos = requisitos,
        validaRequisitoGrado = esPasantia,
        descripcion = TextoLimpio.desdeHtml(description ?: ""),
        urlOriginal = url ?: "",
        origen = "Remotive",
        fechaPublicacion = publicationDate?.take(10)?.takeIf { it.length == 10 },
    )
}
