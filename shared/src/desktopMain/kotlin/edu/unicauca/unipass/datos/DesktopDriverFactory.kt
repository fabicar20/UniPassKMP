package edu.unicauca.unipass.datos

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import edu.unicauca.unipass.db.UniPassDatabase
import java.io.File

class DesktopDriverFactory : DriverFactory {
    override fun crearDriver(): SqlDriver {
        // La base de datos queda en  <carpeta del usuario>/.unipass/unipass.db
        val carpeta = File(System.getProperty("user.home"), ".unipass")
        carpeta.mkdirs()
        val archivo = File(carpeta, "unipass.db")

        val driver = JdbcSqliteDriver("jdbc:sqlite:${archivo.absolutePath}")
        // Las tablas se declaran con CREATE TABLE IF NOT EXISTS, así que es seguro
        // llamarlo en cada arranque.
        UniPassDatabase.Schema.create(driver)
        return driver
    }
}
