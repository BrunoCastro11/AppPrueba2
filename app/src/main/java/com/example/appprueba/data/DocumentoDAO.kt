package com.example.appprueba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DocumentoDAO {

    @Insert
    suspend fun insertar(documento: Documento)

    @Query("SELECT * FROM documentos WHERE propietario = :correo ORDER BY id DESC")
    suspend fun listar(correo: String): List<Documento>

    @Query("DELETE FROM documentos WHERE id = :id")
    suspend fun eliminar(id: Int)
}
