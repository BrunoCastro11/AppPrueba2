package com.example.appprueba.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appprueba.InicioActivity
import com.example.appprueba.ui.components.CampoFormulario
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.Dorado
import com.example.appprueba.ui.theme.VerdeInstitucional
import com.example.appprueba.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(vm: LoginViewModel = viewModel()) {
    val contexto = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeInstitucional)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Blanco),
                contentAlignment = Alignment.Center
            ) {
                Text("FCCh", color = VerdeInstitucional, style = MaterialTheme.typography.titleLarge)
            }

            Text(
                "Fundación Carabineros de Chile",
                color = Blanco,
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blanco)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Iniciar sesión",
                        color = VerdeInstitucional,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    CampoFormulario(
                        valor = vm.correo,
                        onCambio = vm::onCorreoCambio,
                        etiqueta = "Correo",
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

                    Button(
                        onClick = {
                            vm.iniciarSesion { usuario ->
                                val intent = Intent(contexto, InicioActivity::class.java)
                                intent.putExtra("nombre", usuario.nombre)
                                intent.putExtra("correo", usuario.correo)
                                intent.putExtra("rol", usuario.rol)
                                contexto.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                    ) {
                        Text("Ingresar")
                    }
                }
            }
        }
    }
}
