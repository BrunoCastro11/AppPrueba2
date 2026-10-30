package com.example.appprueba

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appprueba.ui.theme.AppPruebaTheme
import com.example.appprueba.ui.theme.Blanco
import com.example.appprueba.ui.theme.Dorado
import com.example.appprueba.ui.theme.VerdeInstitucional

class BienvenidaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPruebaTheme {
                PantallaBienvenida()
            }
        }
    }
}

@Composable
fun PantallaBienvenida() {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Blanco),
                contentAlignment = Alignment.Center
            ) {
                Text("FCCh", color = VerdeInstitucional, style = MaterialTheme.typography.headlineMedium)
            }

            Text(
                "Fundación Carabineros de Chile",
                color = Blanco,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                "Aplicación para docentes e instructores. Carga tus rúbricas, evalúa desde el teléfono y descarga el resultado en PDF.",
                color = Blanco,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    val intent = Intent(contexto, MainActivity::class.java)
                    contexto.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Dorado,
                    contentColor = VerdeInstitucional
                )
            ) {
                Text("Iniciar sesión")
            }

            Button(
                onClick = {
                    val intent = Intent(contexto, RegistroActivity::class.java)
                    contexto.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blanco,
                    contentColor = VerdeInstitucional
                )
            ) {
                Text("Crear cuenta")
            }
        }
    }
}
