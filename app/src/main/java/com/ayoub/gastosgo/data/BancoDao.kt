package com.ayoub.gastosgo.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BancoDao {
    // Insertamos el sueldo. Si ya existe, lo reemplazamos (útil para editar presupuesto en el futuro)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarSaldo(banco: Banco)

    // Recuperamos el sueldo de un usuario concreto
    @Query("SELECT saldoMensual FROM banco WHERE usuarioTitular = :usuario LIMIT 1")
    suspend fun obtenerSueldo(usuario: String): Double?
}