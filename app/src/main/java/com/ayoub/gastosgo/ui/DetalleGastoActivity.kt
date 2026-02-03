package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.GastoDao
import com.example.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetalleGastoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_gasto)

        val idGasto = intent.getIntExtra("ID_GASTO", 0)
        val db = GastosDatabase.getDatabase(this)

        lifecycleScope.launch {
            // Buscamos el gasto por ID.
            // Nota: Podrías necesitar crear 'obtenerGastoPorId' en el DAO si no lo tienes
            // O filtrar la lista, pero lo ideal es query directa: @Query("SELECT * FROM gastos WHERE id = :id")
            // Asumimos que tienes esa funcion en el DAO o la añades ahora.
            val gasto = db.gastoDao().gastoPorId(idGasto) // ¡AÑADE ESTO AL DAO!

            if (gasto != null) {
                findViewById<TextView>(R.id.tvDetalleCantidad).text = "${gasto.cantidad} €"
                findViewById<TextView>(R.id.tvDetalleConcepto).text = gasto.concepto
                findViewById<TextView>(R.id.tvDetalleFecha).text = "${gasto.fecha} - ${gasto.hora}"

                // Mostrar nombre de categoría (Consulta extra para que quede bonito)
                val nombreCategoria = gasto.categoriaId?.let {
                    db.categoriaDao().obtenerNombreCategoria(it)
                } ?: "Sin categoría"
                findViewById<TextView>(R.id.tvDetalleCategoria).text = nombreCategoria

                // Botón Borrar
                findViewById<MaterialButton>(R.id.btnEliminar).setOnClickListener {
                    lifecycleScope.launch {
                        db.gastoDao().borrarGasto(gasto)
                        Toast.makeText(this@DetalleGastoActivity, "Borrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }

    private fun GastoDao.gastoPorId(idGasto: Int) {}
}

