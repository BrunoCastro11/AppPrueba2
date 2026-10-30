package com.example.appprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.appprueba.ui.screens.PantallaInicio
import com.example.appprueba.ui.theme.AppPruebaTheme
import com.example.appprueba.viewmodel.CalificacionesViewModel
import com.example.appprueba.viewmodel.DocumentosViewModel
import com.example.appprueba.viewmodel.InicioViewModel

class InicioActivity : ComponentActivity() {

    private val inicioVm: InicioViewModel by viewModels()
    private val documentosVm: DocumentosViewModel by viewModels()
    private val calificacionesVm: CalificacionesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nombre = intent.getStringExtra("nombre") ?: ""
        val correo = intent.getStringExtra("correo") ?: ""
        val rol = intent.getStringExtra("rol") ?: ""

        inicioVm.iniciar(nombre, correo, rol)
        documentosVm.iniciar(correo)
        calificacionesVm.iniciar(correo)

        setContent {
            AppPruebaTheme {
                PantallaInicio(inicioVm, documentosVm, calificacionesVm)
            }
        }
    }
}
