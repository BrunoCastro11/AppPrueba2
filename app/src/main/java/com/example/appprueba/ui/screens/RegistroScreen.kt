package com.example.appprueba.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appprueba.MainActivity
import com.example.appprueba.ui.components.CampoFormulario
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.Dorado
import com.example.appprueba.ui.theme.VerdeInstitucional
import com.example.appprueba.viewmodel.RegistroViewModel

@Composable
fun PantallaRegistro(vm: RegistroViewModel = viewModel()) {
    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeInstitucional)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            "Fundación Carabineros de Chile",
            color = Blanco,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "Registro de usuarios",
            color = Dorado,
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CampoFormulario(
                    valor = vm.nombre,
                    onCambio = vm::onNombreCambio,
                    etiqueta = "Nombre completo",
                    icono = Icons.Filled.Person,
                    error = vm.errorNombre
                )

                CampoFormulario(
                    valor = vm.correo,
                    onCambio = vm::onCorreoCambio,
                    etiqueta = "Correo (Gmail)",
                    icono = Icons.Filled.Email,
                    error = vm.errorCorreo
                )

                CampoFormulario(
                    valor = vm.contrasena,
                    onCambio = vm::onContrasenaCambio,
                    etiqueta = "Contraseña",
                    icono = Icons.Filled.Lock,
                    error = vm.errorContrasena,
                    oculto = true
                )

                CampoFormulario(
                    valor = vm.confirmacion,
                    onCambio = vm::onConfirmacionCambio,
                    etiqueta = "Repetir contraseña",
                    icono = Icons.Filled.Lock,
                    error = vm.errorConfirmacion,
                    oculto = true
                )

                Text("Tipo de usuario", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = vm.rol == "DOCENTE", onClick = { vm.onRolCambio("DOCENTE") })
                    Text("Docente")
                    RadioButton(selected = vm.rol == "INSTRUCTOR", onClick = { vm.onRolCambio("INSTRUCTOR") })
                    Text("Instructor")
                }

                Button(
                    onClick = {
                        vm.registrar {
                            Toast.makeText(contexto, "Cuenta creada. Ya puedes iniciar sesión", Toast.LENGTH_LONG).show()
                            contexto.startActivity(Intent(contexto, MainActivity::class.java))
                            (contexto as? ComponentActivity)?.finish()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }
}
