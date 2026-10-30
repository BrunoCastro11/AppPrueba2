package com.example.appprueba.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Estado de la pantalla principal: usuario conectado y sección visible.
class InicioViewModel : ViewModel() {

    private var iniciado = false

    var nombre by mutableStateOf("")
        private set
    var correo: String = ""
        private set
    var rol: String = ""
        private set

    // Sección visible: "inicio", "cargar", "ver", "eliminar" o "calificar".
    var seccion by mutableStateOf("inicio")
        private set

    // Recibe los datos del login una sola vez.
    fun iniciar(nombre: String, correo: String, rol: String) {
        if (iniciado) return
        iniciado = true
        this.nombre = nombre
        this.correo = correo
        this.rol = rol
    }

    fun irA(nuevaSeccion: String) {
        seccion = nuevaSeccion
    }

    // Se usa cuando el usuario edita sus datos.
    fun actualizarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
    }
}
