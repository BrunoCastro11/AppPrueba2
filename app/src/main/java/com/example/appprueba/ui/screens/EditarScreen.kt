package com.example.appprueba.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appprueba.ui.components.CampoFormulario
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.Dorado
import com.example.appprueba.ui.theme.VerdeInstitucional
import com.example.appprueba.viewmodel.EditarViewModel

@Composable
fun PantallaEditar(
    vm: EditarViewModel,
    onGuardado: (String) -> Unit,
    onCancelar: () -> Unit
) {
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
            "Editar datos de usuario",
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

                Text("Correo", style = MaterialTheme.typography.titleMedium)
                Text(vm.correo, color = VerdeInstitucional, style = MaterialTheme.typography.bodyLarge)
                Text("El correo no se puede modificar.", style = MaterialTheme.typography.bodyMedium)

                CampoFormulario(
                    valor = vm.nombre,
                    onCambio = vm::onNombreCambio,
                    etiqueta = "Nombre completo",
                    icono = Icons.Filled.Person,
                    error = vm.errorNombre
                )

                Text(
                    "Si no quieres cambiar la contraseña, deja estos campos vacíos.",
                    style = MaterialTheme.typography.bodyMedium
                )

                CampoFormulario(
                    valor = vm.contrasena,
                    onCambio = vm::onContrasenaCambio,
                    etiqueta = "Nueva contraseña",
                    icono = Icons.Filled.Lock,
                    error = vm.errorContrasena,
                    oculto = true
                )

                CampoFormulario(
                    valor = vm.confirmacion,
                    onCambio = vm::onConfirmacionCambio,
                    etiqueta = "Repetir nueva contraseña",
                    icono = Icons.Filled.Lock,
                    error = vm.errorConfirmacion,
                    oculto = true
                )

                Button(
                    onClick = {
                        vm.guardar { nombreNuevo ->
                            Toast.makeText(contexto, "Datos actualizados", Toast.LENGTH_SHORT).show()
                            onGuardado(nombreNuevo)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                ) {
                    Text("Guardar cambios")
                }

                TextButton(
                    onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = VerdeInstitucional)
                }
            }
        }
    }
}
