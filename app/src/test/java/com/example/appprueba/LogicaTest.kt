package com.example.appprueba

import com.example.appprueba.data.Calificacion
import com.example.appprueba.logic.CalificacionLogica
import com.example.appprueba.logic.DocumentoLogica
import com.example.appprueba.logic.LoginLogica
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LogicaTest {


    @Test
    fun correo_vacio_es_invalido() {
        assertEquals("Ingresa tu correo", LoginLogica.validarCorreo(""))
    }

    @Test
    fun correo_sin_formato_es_invalido() {
        assertNotNull(LoginLogica.validarCorreo("abc"))
    }

    @Test
    fun correo_que_no_es_gmail_es_invalido() {
        assertEquals("Debes usar un correo Gmail (@gmail.com)", LoginLogica.validarCorreo("a@yahoo.com"))
    }

    @Test
    fun correo_gmail_es_valido() {
        assertNull(LoginLogica.validarCorreo("docente@gmail.com"))
    }

    @Test
    fun contrasena_corta_es_invalida() {
        assertEquals("Debe tener al menos 6 caracteres", LoginLogica.validarContrasena("12345"))
    }

    @Test
    fun contrasena_valida() {
        assertNull(LoginLogica.validarContrasena("123456"))
    }

    @Test
    fun confirmacion_distinta_es_invalida() {
        assertEquals("Las contraseñas no coinciden", LoginLogica.validarConfirmacion("123456", "654321"))
    }

    @Test
    fun hash_permite_verificar_la_contrasena() {
        val guardada = LoginLogica.hashear("clave123")
        assertTrue(LoginLogica.verificar("clave123", guardada))
        assertFalse(LoginLogica.verificar("otraClave", guardada))
    }

    @Test
    fun hash_usa_sal_distinta_cada_vez() {
        assertNotEquals(LoginLogica.hashear("clave123"), LoginLogica.hashear("clave123"))
    }

    @Test
    fun contrasena_antigua_en_texto_plano_sigue_funcionando() {
        assertTrue(LoginLogica.verificar("clave123", "clave123"))
    }

    // ----- Documentos -----

    @Test
    fun titulo_de_documento_vacio_es_invalido() {
        assertNotNull(DocumentoLogica.validarTitulo(""))
    }

    @Test
    fun titulo_de_documento_valido() {
        assertNull(DocumentoLogica.validarTitulo("Rúbrica de tiro"))
    }

    @Test
    fun archivo_no_seleccionado_es_invalido() {
        assertEquals("Selecciona un archivo", DocumentoLogica.validarArchivo(null))
    }


    @Test
    fun nota_fuera_de_rango_es_invalida() {
        assertNotNull(CalificacionLogica.validarNota("8"))
        assertNotNull(CalificacionLogica.validarNota("0,5"))
    }

    @Test
    fun nota_no_numerica_es_invalida() {
        assertNotNull(CalificacionLogica.validarNota("abc"))
        assertNotNull(CalificacionLogica.validarNota(""))
    }

    @Test
    fun nota_acepta_coma_y_punto() {
        assertNull(CalificacionLogica.validarNota("5,5"))
        assertNull(CalificacionLogica.validarNota("5.5"))
        assertEquals(5.5, CalificacionLogica.convertirNota("5,5")!!, 0.0001)
    }

    @Test
    fun promedio_de_notas() {
        val notas = listOf(nota(6.0), nota(4.0))
        assertEquals(5.0, CalificacionLogica.promedio(notas)!!, 0.0001)
    }

    @Test
    fun promedio_sin_notas_es_nulo() {
        assertNull(CalificacionLogica.promedio(emptyList()))
    }

    @Test
    fun aprobacion_desde_el_4_0() {
        assertTrue(CalificacionLogica.aprobado(4.0))
        assertFalse(CalificacionLogica.aprobado(3.9))
    }

    @Test
    fun nombre_de_alumno_muy_corto_es_invalido() {
        assertNotNull(CalificacionLogica.validarNombreAlumno("ab"))
    }

    private fun nota(valor: Double) = Calificacion(
        alumnoId = 1,
        evaluacion = "Prueba",
        nota = valor,
        comentario = "",
        fecha = ""
    )
}
