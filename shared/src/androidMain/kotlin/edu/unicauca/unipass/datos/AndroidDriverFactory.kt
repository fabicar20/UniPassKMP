package edu.unicauca.unipass.datos

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import edu.unicauca.unipass.db.UniPassDatabase

//esto sirve

class AndroidDriverFactory(private val context: Context) : DriverFactory {
    override fun crearDriver(): SqlDriver =
        AndroidSqliteDriver(UniPassDatabase.Schema, context, "unipass.db")
}
