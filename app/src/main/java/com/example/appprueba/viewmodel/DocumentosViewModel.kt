package com.example.appprueba.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprueba.data.BaseDatos
import com.example.appprueba.data.Documento
import com.example.appprueba.logic.DocumentoLogica
import kotlinx.coroutines.launch

// Estado y acciones de las pantallas cargar, ver y eliminar documentos.
class DocumentosViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BaseDatos.obtener(application).documentoDao()
    private var correo = ""
    private var iniciado = false

    // Lista de documentos del usuario.
    var documentos by mutableStateOf<List<Documento>>(emptyList())
        private set
    var cargando by mutableStateOf(true)
        private set

    // Formulario para cargar un documento.
    var titulo by mutableStateOf("")
        private set
    var nombreArchivo by mutableStateOf<String?>(null)
        private set
    var uriArchivo by mutableStateOf<String?>(null)
        private set
    var errorTitulo by mutableStateOf<String?>(null)
        private set
    var errorArchivo by mutableStateOf<String?>(null)
        private set

    // Documento que espera confirmación para ser eliminado.
    var documentoPorEliminar by mutableStateOf<Documento?>(null)
        private set

    fun iniciar(correo: String) {
        if (iniciado) return
        iniciado = true
        this.correo = correo
        cargarLista()
    }

    // Vuelve a leer los documentos desde la base de datos.
    fun cargarLista() {
        viewModelScope.launch {
            documentos = dao.listar(correo)
            cargando = false
        }
    }

    fun onTituloCambio(valor: String) { titulo = valor; errorTitulo = null }

    fun onArchivoSeleccionado(uri: String, nombre: String) {
        uriArchivo = uri
        nombreArchivo = nombre
        errorArchivo = null
    }

    // Valida y guarda el documento. Si todo sale bien, limpia el formulario.
    fun guardar(onExito: () -> Unit) {
        viewModelScope.launch {
            val resultado = DocumentoLogica.guardar(dao, titulo, nombreArchivo, uriArchivo, correo)
            errorTitulo = resultado.errorTitulo
            errorArchivo = resultado.errorArchivo
            if (resultado.exito) {
                titulo = ""
                uriArchivo = null
                nombreArchivo = null
                documentos = dao.listar(correo)
                onExito()
            }
        }
    }

    fun pedirEliminar(documento: Documento) { documentoPorEliminar = documento }

    fun cancelarEliminar() { documentoPorEliminar = null }

    fun confirmarEliminar(onListo: () -> Unit) {
        val documento = documentoPorEliminar ?: return
        viewModelScope.launch {
            dao.eliminar(documento.id)
            documentos = dao.listar(correo)
            documentoPorEliminar = null
            onListo()
        }
    }
}
