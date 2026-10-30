package com.example.appprueba.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appprueba.ui.theme.Blanco

@Composable
fun TarjetaPendiente(mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Text(mensaje, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyLarge)
    }
}
