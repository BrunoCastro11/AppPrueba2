package com.example.appprueba.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class, Documento::class, Alumno::class, Calificacion::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDAO
    abstract fun documentoDao(): DocumentoDAO
    abstract fun alumnoDao(): AlumnoDAO
}
