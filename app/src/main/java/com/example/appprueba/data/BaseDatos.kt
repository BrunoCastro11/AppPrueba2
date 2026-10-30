package com.example.appprueba.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object BaseDatos {

    private val MIGRACION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `documentos` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`titulo` TEXT NOT NULL, " +
                    "`nombreArchivo` TEXT NOT NULL, " +
                    "`uri` TEXT NOT NULL, " +
                    "`fecha` TEXT NOT NULL, " +
                    "`propietario` TEXT NOT NULL)"
            )
        }
    }

    private val MIGRACION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DELETE FROM usuarios WHERE id NOT IN (SELECT MIN(id) FROM usuarios GROUP BY correo)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_usuarios_correo` ON `usuarios` (`correo`)")
        }
    }

    private val MIGRACION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `alumnos` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`nombre` TEXT NOT NULL, " +
                    "`propietario` TEXT NOT NULL)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `calificaciones` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`alumnoId` INTEGER NOT NULL, " +
                    "`evaluacion` TEXT NOT NULL, " +
                    "`nota` REAL NOT NULL, " +
                    "`comentario` TEXT NOT NULL, " +
                    "`fecha` TEXT NOT NULL, " +
                    "FOREIGN KEY(`alumnoId`) REFERENCES `alumnos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_calificaciones_alumnoId` ON `calificaciones` (`alumnoId`)")
        }
    }

    @Volatile
    private var instancia: AppDatabase? = null

    fun obtener(context: Context): AppDatabase {
        return instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "rubricas.db"
            ).addMigrations(MIGRACION_1_2, MIGRACION_2_3, MIGRACION_3_4).build().also { instancia = it }
        }
    }
}
