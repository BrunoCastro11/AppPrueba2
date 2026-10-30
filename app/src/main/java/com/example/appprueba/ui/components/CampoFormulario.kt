package com.example.appprueba.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.appprueba.ui.theme.RojoError
import com.example.appprueba.ui.theme.VerdeInstitucional


@Composable
fun CampoFormulario(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    oculto: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text
) {
    val colorIcono by animateColorAsState(
        targetValue = if (error != null) RojoError else VerdeInstitucional,
        animationSpec = tween(durationMillis = 400),
        label = "Color ícono $etiqueta"
    )

    TextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = if (oculto) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = { Icon(icono, contentDescription = null, tint = colorIcono) },
        trailingIcon = {
            if (error != null) {
                Icon(Icons.Filled.Warning, contentDescription = "Campo con error", tint = RojoError)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Text(error ?: "", color = RojoError)
    }
}
