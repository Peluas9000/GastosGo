package com.example.gastosgo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val nombreUsuario: String, // La clave es el nombre (ej: "juan")
    val contrasena: String // Guardaremos el HASH, no el texto plano (Bonus seguridad)
)