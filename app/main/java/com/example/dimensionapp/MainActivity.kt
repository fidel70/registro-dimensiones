// app/src/main/java/com/example/dimensionapp/MainActivity.kt

package com.example.dimensionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dimensionapp.data.DatabaseHelper
import com.example.dimensionapp.data.Dimension
import com.example.dimensionapp.data.Pensamiento
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistroDimensiones(dbHelper)
                }
            }
        }
    }
}

@Composable
fun RegistroDimensiones(dbHelper: DatabaseHelper) {
    var pensamientoSeleccionado by remember { mutableStateOf<Pensamiento?>(null) }
    var cantidad by remember { mutableStateOf(0) }
    var duracion by remember { mutableStateOf("") }
    var intensidad by remember { mutableStateOf(0f) }
    
    val pensamientos = remember { dbHelper.obtenerPensamientos() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Paciente: P001",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(pensamientos) { pensamiento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { pensamientoSeleccionado = pensamiento }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = pensamiento.codigo)
                        Text(
                            text = pensamiento.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        pensamientoSeleccionado?.let { pensamiento ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cantidad:")
                        IconButton(onClick = { if (cantidad > 0) cantidad-- }) {
                            Text("-")
                        }
                        Text(cantidad.toString())
                        IconButton(onClick = { cantidad++ }) {
                            Text("+")
                        }
                    }

                    OutlinedTextField(
                        value = duracion,
                        onValueChange = { 
                            if (it.isEmpty() || (it.toIntOrNull() in 0..60)) {
                                duracion = it
                            }
                        },
                        label = { Text("Duración (0-60 min)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Intensidad: ${intensidad.toInt()}")
                    Slider(
                        value = intensidad,
                        onValueChange = { intensidad = it },
                        valueRange = 0f..10f,
                        steps = 9
                    )

                    Button(
                        onClick = {
                            val dimension = Dimension(
                                pensamientoId = pensamiento.codigo,
                                fecha = LocalDate.now().toString(),
                                cantidad = cantidad,
                                duracion = duracion.toIntOrNull(),
                                intensidad = intensidad.toInt()
                            )
                            dbHelper.guardarDimension(dimension)
                            cantidad = 0
                            duracion = ""
                            intensidad = 0f
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
