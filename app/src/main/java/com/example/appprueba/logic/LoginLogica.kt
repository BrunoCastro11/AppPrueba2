package com.example.appprueba.logic

import com.example.appprueba.data.Usuario
import com.example.appprueba.data.UsuarioDAO

import android.database.sqlite.SQLiteConstraintException
import java.security.MessageDigest
import java.security.SecureRandom


data class ResultadoLogin(
    val usuario: Usuario? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null
)

data class ResultadoRegistro(
    val exito: Boolean = false,
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmacion: String? = null
)

object LoginLogica {

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu correo"
            !limpio.contains("@") || !limpio.contains(".") -> "El formato del correo no es válido"
            !limpio.lowercase().endsWith("@gmail.com") -> "Debes usar un correo Gmail (@gmail.com)"
            limpio.length <= "@gmail.com".length -> "El formato del correo no es válido"
            else -> null
        }
    }

    fun validarContrasena(contrasena: String): String? {
        return when {
            contrasena.isEmpty() -> "Ingresa tu contraseña"
            contrasena.length < 6 -> "Debe tener al menos 6 caracteres"
            contrasena.contains(" ") -> "No puede contener espacios"
            else -> null
        }
    }

    fun hashear(contrasena: String): String {
        val sal = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return "${aHex(sal)}:${aHex(sha256(sal, contrasena))}"
    }

    fun verificar(contrasena: String, guardada: String): Boolean {
        val partes = guardada.split(":")
        if (partes.size != 2) return guardada == contrasena
        val sal = partes[0].chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        return aHex(sha256(sal, contrasena)) == partes[1]
    }

    private fun sha256(sal: ByteArray, contrasena: String): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(sal + contrasena.toByteArray(Charsets.UTF_8))

    private fun aHex(bytes: ByteArray): String = bytes.joinToString("") { "%02x".format(it) }

    suspend fun cargarUsuariosFicticios(dao: UsuarioDAO) {
        if (dao.contar() == 0) {
            dao.insertar(Usuario(nombre = "Docente Demo", correo = "docente.demo@gmail.com", contrasena = hashear("docente123"), rol = "DOCENTE"))
            dao.insertar(Usuario(nombre = "Instructor Demo", correo = "instructor.demo@gmail.com", contrasena = hashear("instructor123"), rol = "INSTRUCTOR"))
        }
    }

    suspend fun iniciarSesion(dao: UsuarioDAO, correo: String, contrasena: String): ResultadoLogin {
        val errorCorreo = validarCorreo(correo)
        val errorContrasena = validarContrasena(contrasena)
        if (errorCorreo != null || errorContrasena != null) {
            return ResultadoLogin(errorCorreo = errorCorreo, errorContrasena = errorContrasena)
        }

        cargarUsuariosFicticios(dao)

        val usuario = dao.buscarPorCorreo(correo.trim().lowercase())
        return when {
            usuario == null -> ResultadoLogin(errorCorreo = "No existe una cuenta con este correo")
            !verificar(contrasena, usuario.contrasena) -> ResultadoLogin(errorContrasena = "Contraseña incorrecta")
            else -> {
                if (!usuario.contrasena.contains(":")) {
                    dao.actualizarContrasena(usuario.correo, hashear(contrasena))
                }
                ResultadoLogin(usuario = usuario)
            }
        }
    }

    fun validarNombre(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu nombre"
            limpio.length < 3 -> "Debe tener al menos 3 letras"
            !limpio.all { it.isLetter() || it == ' ' } -> "Solo puede contener letras y espacios"
            else -> null
        }
    }

    fun validarConfirmacion(contrasena: String, confirmacion: String): String? {
        return when {
            confirmacion.isEmpty() -> "Repite tu contraseña"
            confirmacion != contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    suspend fun registrar(
        dao: UsuarioDAO,
        nombre: String,
        correo: String,
        contrasena: String,
        confirmacion: String,
        rol: String
    ): ResultadoRegistro {
        cargarUsuariosFicticios(dao)
        val errorNombre = validarNombre(nombre)
        var errorCorreo = validarCorreo(correo)
        val errorContrasena = validarContrasena(contrasena)
        val errorConfirmacion = validarConfirmacion(contrasena, confirmacion)

        val correoLimpio = correo.trim().lowercase()
        if (errorCorreo == null && dao.buscarPorCorreo(correoLimpio) != null) {
            errorCorreo = "Ya existe una cuenta con este correo"
        }

        if (errorNombre != null || errorCorreo != null || errorContrasena != null || errorConfirmacion != null) {
            return ResultadoRegistro(
                errorNombre = errorNombre,
                errorCorreo = errorCorreo,
                errorContrasena = errorContrasena,
                errorConfirmacion = errorConfirmacion
            )
        }

        return try {
            dao.insertar(Usuario(nombre = nombre.trim(), correo = correoLimpio, contrasena = hashear(contrasena), rol = rol))
            ResultadoRegistro(exito = true)
        } catch (e: SQLiteConstraintException) {
            ResultadoRegistro(errorCorreo = "Ya existe una cuenta con este correo")
        }
    }

    suspend fun editarDatos(
        dao: UsuarioDAO,
        correo: String,
        nombre: String,
        contrasenaNueva: String,
        confirmacion: String
    ): ResultadoRegistro {
        val errorNombre = validarNombre(nombre)

        var errorContrasena: String? = null
        var errorConfirmacion: String? = null
        if (contrasenaNueva.isNotEmpty() || confirmacion.isNotEmpty()) {
            errorContrasena = validarContrasena(contrasenaNueva)
            errorConfirmacion = validarConfirmacion(contrasenaNueva, confirmacion)
        }

        if (errorNombre != null || errorContrasena != null || errorConfirmacion != null) {
            return ResultadoRegistro(
                errorNombre = errorNombre,
                errorContrasena = errorContrasena,
                errorConfirmacion = errorConfirmacion
            )
        }

        dao.actualizarNombre(correo, nombre.trim())
        if (contrasenaNueva.isNotEmpty()) {
            dao.actualizarContrasena(correo, hashear(contrasenaNueva))
        }
        return ResultadoRegistro(exito = true)
    }
}
