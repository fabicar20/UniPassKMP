package edu.unicauca.unipass.dominio

private val PATRON_CORREO = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

/** Validación sencilla de correo electrónico. */
fun String.esCorreoValido(): Boolean = PATRON_CORREO.matches(this)

/** Convierte el HTML que devuelve la API en texto plano legible. */
object TextoLimpio {

    fun desdeHtml(html: String, maximo: Int = 1500): String {
        var texto = html

        // Saltos de línea para etiquetas de bloque
        texto = texto.replace(Regex("<br\\s*/?>|</p>|</li>|</h[1-6]>|</div>", RegexOption.IGNORE_CASE), "\n")
        texto = texto.replace(Regex("<li[^>]*>", RegexOption.IGNORE_CASE), "• ")
        // Quitar el resto de etiquetas
        texto = texto.replace(Regex("<[^>]+>"), "")

        // Entidades HTML más comunes
        val entidades = mapOf(
            "&nbsp;" to " ", "&amp;" to "&", "&lt;" to "<", "&gt;" to ">",
            "&quot;" to "\"", "&#39;" to "'", "&apos;" to "'",
            "&rsquo;" to "'", "&lsquo;" to "'", "&ldquo;" to "\"", "&rdquo;" to "\"",
            "&ndash;" to "-", "&mdash;" to "-", "&hellip;" to "..."
        )
        for ((entidad, reemplazo) in entidades) {
            texto = texto.replace(entidad, reemplazo)
        }

        // Limpiar espacios y líneas en blanco repetidas
        texto = texto.replace(Regex("[ \\t]+"), " ")
        texto = texto.replace(Regex("\\n\\s*\\n(\\s*\\n)+"), "\n\n")
        texto = texto.trim()

        if (texto.length > maximo) {
            texto = texto.take(maximo).trim() + "…"
        }
        return texto
    }
}
