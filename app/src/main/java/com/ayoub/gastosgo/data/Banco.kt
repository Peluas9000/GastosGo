package com.ayoub.gastosgo.data


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "banco",
    foreignKeys = [
        // RELACIÓN: El dinero pertenece a un Usuario. Si borras al usuario, se borra su dinero.
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["nombreUsuario"],
            childColumns = ["usuarioTitular"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Índice para que las búsquedas sean instantáneas
    indices = [Index(value = ["usuarioTitular"])]
)
data class Banco(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioTitular: String, // Clave foránea que une con la tabla Usuarios
    val saldoMensual: Double    // Ej: 1500.0 (Este es tu límite o sueldo)
)