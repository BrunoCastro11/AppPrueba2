package com.example.appprueba.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appprueba.data.Documento
import com.example.appprueba.ui.components.CampoFormulario
import com.example.appprueba.ui.components.TarjetaPendiente
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.RojoError
import com.example.appprueba.ui.theme.VerdeInstitucional
import com.example.appprueba.viewmodel.DocumentosViewModel

fun nombreDeArchivo(contexto: Context, uri: Uri): String {
    var nombre: String? = null
    contexto.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val columna = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (columna >= 0 && cursor.moveToFirst()) {
            nombre = cursor.getString(columna)
        }
    }
    return nombre ?: "documento"
}

fun abrirDocumento(contexto: Context, documento: Documento) {
    val uri = Uri.parse(documento.uri)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, contexto.contentResolver.getType(uri) ?: "*/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        contexto.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(contexto, "No hay una aplicación para abrir este archivo", Toast.LENGTH_LONG).show()
    } catch (e: SecurityException) {
        Toast.makeText(contexto, "No se pudo acceder al archivo", Toast.LENGTH_LONG).show()
    }
}


@Composable
fun PantallaCargar(vm: DocumentosViewModel, onGuardado: () -> Unit) {
    val contexto = LocalContext.current

    val selectorArchivos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
            }
            vm.onArchivoSeleccionado(uri.toString(), nombreDeArchivo(contexto, uri))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampoFormulario(
                    valor = vm.titulo,
                    onCambio = vm::onTituloCambio,
                    etiqueta = "Título del documento",
                    icono = Icons.Filled.Edit,
                    error = vm.errorTitulo
                )

                Button(
                    onClick = { selectorArchivos.launch(arrayOf("*/*")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                ) {
                    Text("Seleccionar archivo")
                }

                Text(
                    text = vm.nombreArchivo ?: "Todavía no se selecciona un archivo",
                    style = MaterialTheme.typography.bodyLarge
                )

                AnimatedVisibility(
                    visible = vm.errorArchivo != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(vm.errorArchivo ?: "", color = RojoError)
                }

                Button(
                    onClick = {
                        vm.guardar {
                            Toast.makeText(contexto, "Documento guardado", Toast.LENGTH_SHORT).show()
                            onGuardado()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                ) {
                    Text("Guardar documento")
                }
            }
        }
    }
}


@Composable
fun PantallaVer(vm: DocumentosViewModel) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) { vm.cargarLista() }

    when {
        vm.cargando -> Text("Cargando documentos...", style = MaterialTheme.typography.bodyLarge)
        vm.documentos.isEmpty() -> TarjetaPendiente("Todavía no hay documentos cargados.")
        else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(vm.documentos, key = { it.id }) { documento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Blanco),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(documento.titulo, color = VerdeInstitucional, style = MaterialTheme.typography.titleMedium)
                        Text(documento.nombreArchivo, style = MaterialTheme.typography.bodyLarge)
                        Text("Cargado el ${documento.fecha}", style = MaterialTheme.typography.bodyMedium)
                        Button(
                            onClick = { abrirDocumento(contexto, documento) },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                        ) {
                            Text("Abrir")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PantallaEliminar(vm: DocumentosViewModel) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) { vm.cargarLista() }

    vm.documentoPorEliminar?.let { documento ->
        AlertDialog(
            onDismissRequest = { vm.cancelarEliminar() },
            title = { Text("Eliminar documento") },
            text = { Text("¿Seguro que quieres eliminar \"${documento.titulo}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.confirmarEliminar {
                            Toast.makeText(contexto, "Documento eliminado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.cancelarEliminar() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    when {
        vm.cargando -> Text("Cargando documentos...", style = MaterialTheme.typography.bodyLarge)
        vm.documentos.isEmpty() -> TarjetaPendiente("No hay documentos para eliminar.")
        else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(vm.documentos, key = { it.id }) { documento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    colors = CardDefaults.cardColors(containerColor = Blanco),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(documento.titulo, color = VerdeInstitucional, style = MaterialTheme.typography.titleMedium)
                            Text(documento.nombreArchivo, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { vm.pedirEliminar(documento) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar ${documento.titulo}", tint = RojoError)
                        }
                    }
                }
            }
        }
    }
}
