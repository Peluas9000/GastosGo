package com.example.gastosgo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Definimos las 3 entidades y subimos la versión por si hubiese cambios futuros
@Database(entities = [Usuario::class, Gasto::class, Categoria::class], version = 1, exportSchema = false)
abstract class GastosDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun gastoDao(): GastoDao
    abstract fun categoriaDao(): CategoriaDao

    companion object {
        @Volatile
        private var INSTANCE: GastosDatabase? = null

        fun getDatabase(context: Context): GastosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GastosDatabase::class.java,
                    "gastos_go_db" // Nombre del archivo físico
                )
                    // Añadimos el callback para pre-cargar datos
                    .addCallback(DatabaseCallback(CoroutineScope(Dispatchers.IO)))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Clase interna para rellenar la BD al crearla
        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Esto se ejecuta SOLO la primera vez que se crea la base de datos
                INSTANCE?.let { database ->
                    scope.launch {
                        val categoriaDao = database.categoriaDao()
                        // Insertamos categorías por defecto
                        categoriaDao.insertar(Categoria(nombre = "Alimentación"))
                        categoriaDao.insertar(Categoria(nombre = "Transporte"))
                        categoriaDao.insertar(Categoria(nombre = "Ocio"))
                        categoriaDao.insertar(Categoria(nombre = "Salud"))
                        categoriaDao.insertar(Categoria(nombre = "Hogar"))
                        categoriaDao.insertar(Categoria(nombre = "Otros"))
                    }
                }
            }
        }
    }
}