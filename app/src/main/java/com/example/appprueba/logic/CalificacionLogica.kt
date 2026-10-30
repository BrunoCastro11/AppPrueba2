package com.example.appprueba.logic

import com.example.appprueba.data.Alumno
import com.example.appprueba.data.AlumnoDAO
import com.example.appprueba.data.Calificacion

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ResultadoAlumno(
    val exito: Boolean = false,
    val errorNombre: String? = null
)

data class ResultadoNota(
    val exito: Boolean = false,
    val errorEvaluacion: String? = null,
    val errorNota: String? = null,
    val errorComentario: String? = null
)

object CalificacionLogica {

    const val NOTA_MINIMA = 1.0
    const val NOTA_MAXIMA = 7.0
    const val NOTA_APROBACION = 4.0

    fun validarNombreAlumno(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "Ingresa el nombre del alumno"
            limpio.length < 3 -> "Debe tener al menos 3 caracteres"
            limpio.length > 60 -> "Máximo 60 caracteres"
            else -> null
        }
    }

    fun validarEvaluacion(evaluacion: String): String? {
        val limpio = evaluacion.trim()
        return when {
            limpio.isEmpty() -> "Ingresa el nombre de la evaluación"
            limpio.length > 40 -> "Máximo 40 caracteres"
            else -> null
        }
    }

    fun convertirNota(texto: String): Double? =
        texto.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() }

    fun validarNota(texto: String): String? {
        if (texto.isBlank()) return "Ingresa la nota"
        val nota = convertirNota(texto) ?: return "Ingresa un número válido (ej: 5,5)"
        return if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) "La nota debe estar entre 1,0 y 7,0" else null
    }

    fun validarComentario(comentario: String): String? =
        if (comentario.trim().length > 120) "Máximo 120 caracteres" else null

    fun promedio(notas: List<Calificacion>): Double? =
        if (notas.isEmpty()) null else notas.map { it.nota }.average()

    fun aprobado(nota: Double): Boolean = nota >= NOTA_APROBACION

    fun formatearNota(nota: Double): String = String.format(Locale.getDefault(), "%.1f", nota)

    suspend fun guardarAlumno(dao: AlumnoDAO, nombre: String, propietario: String): ResultadoAlumno {
        var error = validarNombreAlumno(nombre)
        if (error == null && dao.contarNombre(propietario, nombre.trim()) > 0) {
            error = "Ya registraste un alumno con ese nombre"
        }
        if (error != null) return ResultadoAlumno(errorNombre = error)

        dao.insertar(Alumno(nombre = nombre.trim(), propietario = propietario))
        return ResultadoAlumno(exito = true)
    }

    suspend fun guardarNota(
        dao: AlumnoDAO,
        alumnoId: Int,
        evaluacion: String,
        notaTexto: String,
        comentario: String
    ): ResultadoNota {
        val errorEvaluacion = validarEvaluacion(evaluacion)
        val errorNota = validarNota(notaTexto)
        val errorComentario = validarComentario(comentario)
        val nota = convertirNota(notaTexto)
        if (errorEvaluacion != null || errorNota != null || errorComentario != null || nota == null) {
            return ResultadoNota(
                errorEvaluacion = errorEvaluacion,
                errorNota = errorNota,
                errorComentario = errorComentario
            )
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        dao.insertarNota(
            Calificacion(
                alumnoId = alumnoId,
                evaluacion = evaluacion.trim(),
                nota = nota,
                comentario = comentario.trim(),
                fecha = fecha
            )
        )
        return ResultadoNota(exito = true)
    }
}
