package com.example.gastosgo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    // Si intentan registrar un nombre que ya existe, cancela la operación (ABORT)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarUsuario(usuario: Usuario)

    // Busca usuario y contraseña hash. Devuelve null si no existe.
    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :user AND contrasena = :pass")
    suspend fun login(user: String, pass: String): Usuario?
}