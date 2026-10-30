package com.example.appprueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprueba.data.BaseDatos
import com.example.appprueba.logic.LoginLogica
import kotlinx.coroutines.launch

// Estado y acciones de la pantalla de registro.
class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BaseDatos.obtener(application).usuarioDao()

    var nombre by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmacion by mutableStateOf("")
        private set
    var rol by mutableStateOf("DOCENTE")
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmacion by mutableStateOf<String?>(null)
        private set

    fun onNombreCambio(valor: String) { nombre = valor; errorNombre = null }
    fun onCorreoCambio(valor: String) { correo = valor; errorCorreo = null }
    fun onContrasenaCambio(valor: String) { contrasena = valor; errorContrasena = null }
    fun onConfirmacionCambio(valor: String) { confirmacion = valor; errorConfirmacion = null }
    fun onRolCambio(valor: String) { rol = valor }

    // Valida todos los campos y, si están bien, crea la cuenta.
    fun registrar(onExito: () -> Unit) {
        viewModelScope.launch {
            val resultado = LoginLogica.registrar(dao, nombre, correo, contrasena, confirmacion, rol)
            errorNombre = resultado.errorNombre
            errorCorreo = resultado.errorCorreo
            errorContrasena = resultado.errorContrasena
            errorConfirmacion = resultado.errorConfirmacion
            if (resultado.exito) onExito()
        }
    }
}
