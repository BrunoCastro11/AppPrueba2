package com.example.appprueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprueba.data.Alumno
import com.example.appprueba.data.BaseDatos
import com.example.appprueba.data.Calificacion
import com.example.appprueba.logic.CalificacionLogica
import kotlinx.coroutines.launch

// Estado y acciones de la sección para calificar alumnos.
class CalificacionesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BaseDatos.obtener(application).alumnoDao()
    private var correo = ""
    private var iniciado = false

    var alumnos by mutableStateOf<List<Alumno>>(emptyList())
        private set
    var notas by mutableStateOf<List<Calificacion>>(emptyList())
        private set
    var cargando by mutableStateOf(true)
        private set

    // Formulario "Nuevo alumno".
    var nombreNuevo by mutableStateOf("")
        private set
    var errorNombre by mutableStateOf<String?>(null)
        private set

    // Alumno cuya tarjeta está desplegada.
    var expandidoId by mutableStateOf<Int?>(null)
        private set

    // Ventana "Agregar nota": alumno elegido y campos del formulario.
    var alumnoParaNota by mutableStateOf<Alumno?>(null)
        private set
    var evaluacion by mutableStateOf("")
        private set
    var notaTexto by mutableStateOf("")
        private set
    var comentario by mutableStateOf("")
        private set
    var errorEvaluacion by mutableStateOf<String?>(null)
        private set
    var errorNota by mutableStateOf<String?>(null)
        private set
    var errorComentario by mutableStateOf<String?>(null)
        private set

    // Alumno que espera confirmación para ser eliminado.
    var alumnoPorEliminar by mutableStateOf<Alumno?>(null)
        private set

    fun iniciar(correo: String) {
        if (iniciado) return
        iniciado = true
        this.correo = correo
        viewModelScope.launch { recargar() }
    }

    private suspend fun recargar() {
        alumnos = dao.listar(correo)
        notas = dao.listarNotas(correo)
        cargando = false
    }

    // Notas de un alumno en particular.
    fun notasDe(alumnoId: Int): List<Calificacion> = notas.filter { it.alumnoId == alumnoId }

    // ----- Alumnos -----

    fun onNombreCambio(valor: String) { nombreNuevo = valor; errorNombre = null }

    fun agregarAlumno(onListo: () -> Unit) {
        viewModelScope.launch {
            val resultado = CalificacionLogica.guardarAlumno(dao, nombreNuevo, correo)
            errorNombre = resultado.errorNombre
            if (resultado.exito) {
                nombreNuevo = ""
                recargar()
                onListo()
            }
        }
    }

    fun alternarExpandido(alumnoId: Int) {
        expandidoId = if (expandidoId == alumnoId) null else alumnoId
    }

    fun pedirEliminarAlumno(alumno: Alumno) { alumnoPorEliminar = alumno }

    fun cancelarEliminarAlumno() { alumnoPorEliminar = null }

    fun confirmarEliminarAlumno(onListo: () -> Unit) {
        val alumno = alumnoPorEliminar ?: return
        viewModelScope.launch {
            dao.eliminar(alumno.id)
            recargar()
            alumnoPorEliminar = null
            onListo()
        }
    }

    // ----- Notas -----

    fun abrirDialogoNota(alumno: Alumno) {
        evaluacion = ""
        notaTexto = ""
        comentario = ""
        errorEvaluacion = null
        errorNota = null
        errorComentario = null
        alumnoParaNota = alumno
    }

    fun cerrarDialogoNota() { alumnoParaNota = null }

    fun onEvaluacionCambio(valor: String) { evaluacion = valor; errorEvaluacion = null }
    fun onNotaCambio(valor: String) { notaTexto = valor; errorNota = null }
    fun onComentarioCambio(valor: String) { comentario = valor; errorComentario = null }

    fun guardarNota(onListo: () -> Unit) {
        val alumno = alumnoParaNota ?: return
        viewModelScope.launch {
            val resultado = CalificacionLogica.guardarNota(
                dao, alumno.id, evaluacion, notaTexto, comentario
            )
            errorEvaluacion = resultado.errorEvaluacion
            errorNota = resultado.errorNota
            errorComentario = resultado.errorComentario
            if (resultado.exito) {
                alumnoParaNota = null
                expandidoId = alumno.id
                recargar()
                onListo()
            }
        }
    }

    fun eliminarNota(nota: Calificacion) {
        viewModelScope.launch {
            dao.eliminarNota(nota.id)
            recargar()
        }
    }
}
