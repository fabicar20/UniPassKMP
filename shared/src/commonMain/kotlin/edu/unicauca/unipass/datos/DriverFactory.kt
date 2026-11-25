package edu.unicauca.unipass.datos

import app.cash.sqldelight.db.SqlDriver

/**
 * Cada plataforma sabe crear su propio "driver" de SQLite:
 *  - Android: AndroidSqliteDriver (necesita un Context)
 *  - Desktop: JdbcSqliteDriver (archivo en la carpeta del usuario)
 */
interface DriverFactory {
    fun crearDriver(): SqlDriver
}
