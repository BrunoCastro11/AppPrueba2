package com.example.appprueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprueba.data.BaseDatos
import com.example.appprueba.data.Usuario
import com.example.appprueba.logic.LoginLogica
import kotlinx.coroutines.launch

// Estado y acciones de la pantalla de login. No conoce ningún componente visual.
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BaseDatos.obtener(application).usuarioDao()

    // Estado observado por la pantalla. Solo el ViewModel puede modificarlo.
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set

    fun onCorreoCambio(valor: String) {
        correo = valor
        errorCorreo = null // Al escribir se limpia el error anterior.
    }

    fun onContrasenaCambio(valor: String) {
        contrasena = valor
        errorContrasena = null
    }

    // Valida y consulta la base de datos. Si es correcto, avisa a la pantalla con el usuario.
    fun iniciarSesion(onExito: (Usuario) -> Unit) {
        viewModelScope.launch {
            val resultado = LoginLogica.iniciarSesion(dao, correo, contrasena)
            errorCorreo = resultado.errorCorreo
            errorContrasena = resultado.errorContrasena
            resultado.usuario?.let { usuario ->
                contrasena = "" // No se deja la contraseña en memoria.
                onExito(usuario)
            }
        }
    }
}
