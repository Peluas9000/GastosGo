package com.ayoub.gastosgo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String // Ej: "Comida", "Transporte", "Ocio"
)