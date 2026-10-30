package com.example.appprueba

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.appprueba.ui.screens.PantallaEditar
import com.example.appprueba.ui.theme.AppPruebaTheme
import com.example.appprueba.viewmodel.EditarViewModel

class EditarActivity : ComponentActivity() {

    private val vm: EditarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val correo = intent.getStringExtra("correo") ?: ""
        val nombre = intent.getStringExtra("nombre") ?: ""
        vm.iniciar(correo, nombre)

        setContent {
            AppPruebaTheme {
                PantallaEditar(
                    vm = vm,
                    onGuardado = { nuevoNombre ->
                        setResult(Activity.RESULT_OK, Intent().putExtra("nombre", nuevoNombre))
                        finish()
                    },
                    onCancelar = { finish() }
                )
            }
        }
    }
}
