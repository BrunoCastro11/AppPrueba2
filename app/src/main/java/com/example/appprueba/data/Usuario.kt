package com.example.appprueba.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val correo: String,

    val contrasena: String,

    val rol: String
)
