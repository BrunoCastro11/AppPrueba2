package com.example.appprueba.ui.screens

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.appprueba.BienvenidaActivity
import com.example.appprueba.EditarActivity
import com.example.appprueba.viewmodel.CalificacionesViewModel
import com.example.appprueba.viewmodel.DocumentosViewModel
import com.example.appprueba.viewmodel.InicioViewModel
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.Dorado
import com.example.appprueba.ui.theme.VerdeInstitucional

const val NUMERO_SOPORTE = "+5691234556"

@Composable
fun PantallaInicio(
    vm: InicioViewModel,
    documentosVm: DocumentosViewModel,
    calificacionesVm: CalificacionesViewModel
) {
    val contexto = LocalContext.current

    val lanzadorEditar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            resultado.data?.getStringExtra("nombre")?.let { vm.actualizarNombre(it) }
        }
    }

    val titulo = when (vm.seccion) {
        "cargar" -> "Cargar documentos"
        "ver" -> "Ver documentos"
        "eliminar" -> "Eliminar documento"
        "calificar" -> "Calificar alumnos"
        else -> "Inicio"
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = titulo,
                onInicio = { vm.irA("inicio") },
                onEditar = {
                    val intent = Intent(contexto, EditarActivity::class.java)
                    intent.putExtra("correo", vm.correo)
                    intent.putExtra("nombre", vm.nombre)
                    lanzadorEditar.launch(intent)
                },
                onCerrarSesion = {
                    // Vuelve a la bienvenida y borra las pantallas anteriores.
                    val intent = Intent(contexto, BienvenidaActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    contexto.startActivity(intent)
                }
            )
        },
        bottomBar = {
            BarraInferior(seccion = vm.seccion, onSeleccion = { vm.irA(it) })
        }
    ) { relleno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp)
        ) {
            when (vm.seccion) {
                "cargar" -> PantallaCargar(documentosVm, onGuardado = { vm.irA("ver") })
                "ver" -> PantallaVer(documentosVm)
                "eliminar" -> PantallaEliminar(documentosVm)
                "calificar" -> PantallaCalificar(calificacionesVm)
                else -> VistaBienvenida(vm.nombre, vm.rol)
            }
        }
    }
}

@Composable
fun BarraSuperior(
    titulo: String,
    onInicio: () -> Unit,
    onEditar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeInstitucional)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = Blanco,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onInicio) {
            Icon(Icons.Filled.Home, contentDescription = "Ir al inicio", tint = Blanco)
        }
        IconButton(onClick = onEditar) {
            Icon(Icons.Filled.Edit, contentDescription = "Editar datos de usuario", tint = Blanco)
        }
        IconButton(onClick = onCerrarSesion) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión", tint = Dorado)
        }
    }
}

@Composable
fun BarraInferior(seccion: String, onSeleccion: (String) -> Unit) {
    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = VerdeInstitucional,
        selectedTextColor = Dorado,
        indicatorColor = Dorado,
        unselectedIconColor = Blanco,
        unselectedTextColor = Blanco
    )

    NavigationBar(containerColor = VerdeInstitucional) {
        NavigationBarItem(
            selected = seccion == "cargar",
            onClick = { onSeleccion("cargar") },
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            label = { Text("Cargar") },
            colors = colores
        )
        NavigationBarItem(
            selected = seccion == "ver",
            onClick = { onSeleccion("ver") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            label = { Text("Ver") },
            colors = colores
        )
        NavigationBarItem(
            selected = seccion == "eliminar",
            onClick = { onSeleccion("eliminar") },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
            label = { Text("Eliminar") },
            colors = colores
        )
        NavigationBarItem(
            selected = seccion == "calificar",
            onClick = { onSeleccion("calificar") },
            icon = { Icon(Icons.Filled.Star, contentDescription = null) },
            label = { Text("Calificar") },
            colors = colores
        )
    }
}

@Composable
fun VistaBienvenida(nombre: String, rol: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Bienvenido/a", color = Dorado, style = MaterialTheme.typography.titleMedium)
                Text(nombre, color = VerdeInstitucional, style = MaterialTheme.typography.headlineMedium)
                Text(
                    if (rol == "INSTRUCTOR") "Instructor" else "Docente",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Text(
                "Usa la barra inferior para cargar, ver o eliminar documentos, o para calificar alumnos. " +
                    "Arriba puedes editar tus datos o cerrar sesión.",
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        TarjetaSoporte()
    }
}

@Composable
fun TarjetaSoporte() {
    val context = LocalContext.current

    fun llamar() {
        // Comprobar el permiso antes de iniciar la llamada.
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", NUMERO_SOPORTE, null))

        try {
            context.startActivity(intent)
        } catch (e: SecurityException) {
            Toast.makeText(context, "Falta autorización", Toast.LENGTH_LONG).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No hay una app para realizar llamadas", Toast.LENGTH_LONG).show()
        }
    }

    val solicitarPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            llamar()
        } else {
            Toast.makeText(context, "Permiso de llamadas denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Soporte", color = VerdeInstitucional, style = MaterialTheme.typography.titleMedium)
            Text(
                "Si tienes un problema con la aplicación, llama al equipo de soporte.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = {
                    val permisoConcedido = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED

                    if (permisoConcedido) {
                        llamar()
                    } else {
                        solicitarPermiso.launch(Manifest.permission.CALL_PHONE)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
            ) {
                Text("Llamar a soporte")
            }
        }
    }
}
