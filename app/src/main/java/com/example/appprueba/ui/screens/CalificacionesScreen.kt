package com.example.appprueba.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appprueba.data.Alumno
import com.example.appprueba.data.Calificacion
import com.example.appprueba.logic.CalificacionLogica
import com.example.appprueba.ui.components.CampoFormulario
import com.example.appprueba.ui.components.TarjetaPendiente
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.RojoError
import com.example.appprueba.ui.theme.VerdeInstitucional
import com.example.appprueba.viewmodel.CalificacionesViewModel


@Composable
fun PantallaCalificar(vm: CalificacionesViewModel) {
    val contexto = LocalContext.current

    vm.alumnoParaNota?.let { alumno ->
        DialogoNota(
            vm = vm,
            alumno = alumno,
            onGuardada = { Toast.makeText(contexto, "Nota guardada", Toast.LENGTH_SHORT).show() }
        )
    }

    vm.alumnoPorEliminar?.let { alumno ->
        AlertDialog(
            onDismissRequest = { vm.cancelarEliminarAlumno() },
            title = { Text("Eliminar alumno") },
            text = { Text("¿Seguro que quieres eliminar a \"${alumno.nombre}\" y todas sus notas? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.confirmarEliminarAlumno {
                            Toast.makeText(contexto, "Alumno eliminado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.cancelarEliminarAlumno() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blanco),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nuevo alumno", color = VerdeInstitucional, style = MaterialTheme.typography.titleMedium)
                    CampoFormulario(
                        valor = vm.nombreNuevo,
                        onCambio = vm::onNombreCambio,
                        etiqueta = "Nombre del alumno",
                        icono = Icons.Filled.Person,
                        error = vm.errorNombre
                    )
                    Button(
                        onClick = {
                            vm.agregarAlumno {
                                Toast.makeText(contexto, "Alumno agregado", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                    ) {
                        Text("Agregar alumno")
                    }
                }
            }
        }

        if (vm.cargando) {
            item { Text("Cargando alumnos...", style = MaterialTheme.typography.bodyLarge) }
        } else if (vm.alumnos.isEmpty()) {
            item { TarjetaPendiente("Todavía no hay alumnos. Agrega uno para comenzar a calificar.") }
        } else {
            items(vm.alumnos, key = { it.id }) { alumno ->
                TarjetaAlumno(
                    alumno = alumno,
                    notas = vm.notasDe(alumno.id),
                    expandido = vm.expandidoId == alumno.id,
                    modifier = Modifier.animateItem(),
                    onAlternar = { vm.alternarExpandido(alumno.id) },
                    onAgregarNota = { vm.abrirDialogoNota(alumno) },
                    onEliminarAlumno = { vm.pedirEliminarAlumno(alumno) },
                    onEliminarNota = { nota -> vm.eliminarNota(nota) }
                )
            }
        }
    }
}

@Composable
fun TarjetaAlumno(
    alumno: Alumno,
    notas: List<Calificacion>,
    expandido: Boolean,
    modifier: Modifier = Modifier,
    onAlternar: () -> Unit,
    onAgregarNota: () -> Unit,
    onEliminarAlumno: () -> Unit,
    onEliminarNota: (Calificacion) -> Unit
) {
    val promedio = CalificacionLogica.promedio(notas)

    val colorPromedio by animateColorAsState(
        targetValue = when {
            promedio == null -> Color.Gray
            CalificacionLogica.aprobado(promedio) -> VerdeInstitucional
            else -> RojoError
        },
        animationSpec = tween(durationMillis = 400),
        label = "Color promedio ${alumno.nombre}"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 400)),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAlternar),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = VerdeInstitucional)
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(alumno.nombre, color = VerdeInstitucional, style = MaterialTheme.typography.titleMedium)
                    Text("${notas.size} evaluación(es)", style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = if (promedio == null) "Sin notas" else CalificacionLogica.formatearNota(promedio),
                    color = colorPromedio,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            AnimatedVisibility(
                visible = expandido,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (notas.isEmpty()) {
                        Text("Todavía no hay notas para este alumno.", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        notas.forEach { nota ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${nota.evaluacion}: ${CalificacionLogica.formatearNota(nota.nota)}",
                                        color = if (CalificacionLogica.aprobado(nota.nota)) VerdeInstitucional else RojoError,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    if (nota.comentario.isNotBlank()) {
                                        Text(nota.comentario, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Text(nota.fecha, style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { onEliminarNota(nota) }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Eliminar nota ${nota.evaluacion}",
                                        tint = RojoError
                                    )
                                }
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onAgregarNota,
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar nota")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = onEliminarAlumno) {
                            Text("Eliminar alumno", color = RojoError)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogoNota(
    vm: CalificacionesViewModel,
    alumno: Alumno,
    onGuardada: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { vm.cerrarDialogoNota() },
        title = { Text("Nota para ${alumno.nombre}") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CampoFormulario(
                    valor = vm.evaluacion,
                    onCambio = vm::onEvaluacionCambio,
                    etiqueta = "Evaluación (ej: Prueba 1)",
                    icono = Icons.Filled.Edit,
                    error = vm.errorEvaluacion
                )
                CampoFormulario(
                    valor = vm.notaTexto,
                    onCambio = vm::onNotaCambio,
                    etiqueta = "Nota (1,0 a 7,0)",
                    icono = Icons.Filled.Star,
                    error = vm.errorNota,
                    teclado = KeyboardType.Decimal
                )
                CampoFormulario(
                    valor = vm.comentario,
                    onCambio = vm::onComentarioCambio,
                    etiqueta = "Comentario (opcional)",
                    icono = Icons.Filled.Create,
                    error = vm.errorComentario
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { vm.guardarNota(onGuardada) },
                colors = ButtonDefaults.buttonColors(containerColor = VerdeInstitucional)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { vm.cerrarDialogoNota() }) {
                Text("Cancelar")
            }
        }
    )
}
