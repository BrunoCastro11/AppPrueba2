package com.example.appprueba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlumnoDAO {


    @Insert
    suspend fun insertar(alumno: Alumno)

    @Query("SELECT * FROM alumnos WHERE propietario = :correo ORDER BY nombre COLLATE NOCASE")
    suspend fun listar(correo: String): List<Alumno>

    @Query("SELECT COUNT(*) FROM alumnos WHERE propietario = :correo AND LOWER(nombre) = LOWER(:nombre)")
    suspend fun contarNombre(correo: String, nombre: String): Int

    @Query("DELETE FROM alumnos WHERE id = :id")
    suspend fun eliminar(id: Int)


    @Insert
    suspend fun insertarNota(calificacion: Calificacion)

    @Query(
        "SELECT c.* FROM calificaciones c " +
            "INNER JOIN alumnos a ON a.id = c.alumnoId " +
            "WHERE a.propietario = :correo ORDER BY c.id DESC"
    )
    suspend fun listarNotas(correo: String): List<Calificacion>

    @Query("DELETE FROM calificaciones WHERE id = :id")
    suspend fun eliminarNota(id: Int)
}
