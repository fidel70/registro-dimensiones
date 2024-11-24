// app/src/main/java/com/example/dimensionapp/data/Models.kt

package com.example.dimensionapp.data

data class Pensamiento(
    val codigo: String,
    val descripcion: String
)

data class Dimension(
    val id: Int = 0,
    val pensamientoId: String,
    val fecha: String,
    val cantidad: Int,
    val duracion: Int?,
    val intensidad: Int
)

val PENSAMIENTOS_P001 = listOf(
    Pensamiento("P001-1", "Primer pensamiento..."),
    Pensamiento("P001-2", "Segundo pensamiento...")
    // Añade aquí los pensamientos reales de P001
)
