package com.example.appprueba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documentos")
data class Documento(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,

    val nombreArchivo: String,

    val uri: String,

    val fecha: String,

    val propietario: String
)
