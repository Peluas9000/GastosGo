package com.ayoub.gastosgo.data // Asegúrate de que este paquete coincida con el tuyo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gastosgo.data.Gasto

@Dao
interface GastoDao {
    @Insert
    suspend fun insertarGasto(gasto: Gasto)

    @Delete
    suspend fun borrarGasto(gasto: Gasto)

    @Query("SELECT * FROM gastos WHERE usuarioAutor = :usuario ORDER BY id DESC")
    suspend fun obtenerGastos(usuario: String): List<Gasto>

    @Query("SELECT * FROM gastos WHERE usuarioAutor = :usuario ORDER BY cantidad DESC")
    suspend fun obtenerGastosPorPrecio(usuario: String): List<Gasto>

    @Query("SELECT SUM(cantidad) FROM gastos WHERE usuarioAutor = :usuario")
    suspend fun obtenerTotalGastado(usuario: String): Double?

    // --- ¡ESTA ES LA LÍNEA QUE TE FALTA! AÑÁDELA AQUÍ ABAJO ---
    @Query("SELECT * FROM gastos WHERE id = :id")
    suspend fun obtenerGastoPorId(id: Int): Gasto?
}