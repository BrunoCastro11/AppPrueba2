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

// Estado y acciones de la pantalla para editar los datos del usuario.
class EditarViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BaseDatos.obtener(application).usuarioDao()
    private var iniciado = false

    // El correo identifica la cuenta y no se puede cambiar.
    var correo: String = ""
        private set

    var nombre by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmacion by mutableStateOf("")
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmacion by mutableStateOf<String?>(null)
        private set

    // Se llama una sola vez con los datos recibidos; al girar el teléfono no se pierde lo escrito.
    fun iniciar(correo: String, nombreInicial: String) {
        if (iniciado) return
        iniciado = true
        this.correo = correo
        nombre = nombreInicial
    }

    fun onNombreCambio(valor: String) { nombre = valor; errorNombre = null }
    fun onContrasenaCambio(valor: String) { contrasena = valor; errorContrasena = null }
    fun onConfirmacionCambio(valor: String) { confirmacion = valor; errorConfirmacion = null }

    // Si todo es válido, guarda los cambios y entrega el nombre nuevo.
    fun guardar(onExito: (String) -> Unit) {
        viewModelScope.launch {
            val resultado = LoginLogica.editarDatos(dao, correo, nombre, contrasena, confirmacion)
            errorNombre = resultado.errorNombre
            errorContrasena = resultado.errorContrasena
            errorConfirmacion = resultado.errorConfirmacion
            if (resultado.exito) onExito(nombre.trim())
        }
    }
}
