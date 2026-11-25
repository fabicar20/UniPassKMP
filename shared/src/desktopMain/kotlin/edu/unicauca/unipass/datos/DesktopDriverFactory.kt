package edu.unicauca.unipass.datos

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import edu.unicauca.unipass.db.UniPassDatabase
import java.io.File
import java.util.Properties

/**
 * Desktop: SQLite con el driver JDBC de SQLDelight.
 *
 * Se crea como indica la documentación de SQLDelight para JVM: se le pasa el `Schema`
 * al driver y él se encarga de todo usando `PRAGMA user_version`:
 *  - Si la base es nueva (versión 0), crea las tablas y guarda la versión actual.
 *  - Si la versión guardada es menor que la del esquema, ejecuta las migraciones.
 *  - Si ya está al día, no hace nada.
 * Es lo mismo que hace AndroidSqliteDriver en Android.
 */
class DesktopDriverFactory : DriverFactory {
    override fun crearDriver(): SqlDriver {
        // La base de datos queda en  <carpeta del usuario>/.unipass/unipass.db
        val carpeta = File(System.getProperty("user.home"), ".unipass")
        carpeta.mkdirs()
        val archivo = File(carpeta, "unipass.db")

        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${archivo.absolutePath}",
            properties = Properties(),
            schema = UniPassDatabase.Schema,
        )
    }
}
