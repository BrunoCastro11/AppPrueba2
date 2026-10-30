package com.example.appprueba.logic

import com.example.appprueba.data.Documento
import com.example.appprueba.data.DocumentoDAO

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ResultadoDocumento(
    val exito: Boolean = false,
    val errorTitulo: String? = null,
    val errorArchivo: String? = null
)

object DocumentoLogica {

    fun validarTitulo(titulo: String): String? {
        val limpio = titulo.trim()
        return when {
            limpio.isEmpty() -> "Ingresa un título para el documento"
            limpio.length < 3 -> "Debe tener al menos 3 caracteres"
            limpio.length > 60 -> "Máximo 60 caracteres"
            else -> null
        }
    }

    fun validarArchivo(uri: String?): String? {
        return if (uri == null) "Selecciona un archivo" else null
    }

    suspend fun guardar(
        dao: DocumentoDAO,
        titulo: String,
        nombreArchivo: String?,
        uri: String?,
        propietario: String
    ): ResultadoDocumento {
        val errorTitulo = validarTitulo(titulo)
        val errorArchivo = validarArchivo(uri)
        if (errorTitulo != null || errorArchivo != null || uri == null) {
            return ResultadoDocumento(errorTitulo = errorTitulo, errorArchivo = errorArchivo)
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        dao.insertar(
            Documento(
                titulo = titulo.trim(),
                nombreArchivo = nombreArchivo ?: "documento",
                uri = uri,
                fecha = fecha,
                propietario = propietario
            )
        )
        return ResultadoDocumento(exito = true)
    }
}
