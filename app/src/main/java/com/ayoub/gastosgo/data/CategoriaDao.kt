package com.example.gastosgo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoriaDao {
    @Insert
    suspend fun insertar(categoria: Categoria)

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    suspend fun obtenerTodas(): List<Categoria>

    // Para saber si la BD está vacía al iniciar la app
    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contar(): Int

    // Helper para obtener el nombre de una categoría por su ID
    @Query("SELECT nombre FROM categorias WHERE id = :id")
    suspend fun obtenerNombreCategoria(id: Int): String?
}