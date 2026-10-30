package com.example.appprueba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDAO {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("UPDATE usuarios SET nombre = :nombre WHERE correo = :correo")
    suspend fun actualizarNombre(correo: String, nombre: String)

    @Query("UPDATE usuarios SET contrasena = :contrasena WHERE correo = :correo")
    suspend fun actualizarContrasena(correo: String, contrasena: String)
}
