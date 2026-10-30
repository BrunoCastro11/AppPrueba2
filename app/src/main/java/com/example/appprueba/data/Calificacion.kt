package com.example.appprueba.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificaciones",
    foreignKeys = [
        ForeignKey(
            entity = Alumno::class,
            parentColumns = ["id"],
            childColumns = ["alumnoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["alumnoId"])]
)
data class Calificacion(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val alumnoId: Int,

    val evaluacion: String,

    val nota: Double,

    val comentario: String,

    val fecha: String
)
