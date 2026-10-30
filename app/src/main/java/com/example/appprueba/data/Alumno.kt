package com.example.appprueba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alumnos")
data class Alumno(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val propietario: String
)
