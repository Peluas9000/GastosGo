package com.ayoub.gastosgo.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gastos",
    foreignKeys = [
        // Relación 1: Un gasto pertenece a un Usuario (Si borras usuario, se borran sus gastos)
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["nombreUsuario"],
            childColumns = ["usuarioAutor"],
            onDelete = ForeignKey.CASCADE
        ),
        // Relación 2: Un gasto tiene una Categoría (Si borras la categoría, el campo se pone a NULL)
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioAutor: String, // FK -> Usuario
    val categoriaId: Int?,    // FK -> Categoria (Puede ser null si se borra la categoría)
    val concepto: String,
    val fecha: String,        // Formato String para simplificar: "25/02/2026"
    val hora: String,
    val cantidad: Double      // Usamos Double para el dinero
)