package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.Gasto
import com.ayoub.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DetalleGastoActivity : AppCompatActivity() {

    private lateinit var db: GastosDatabase
    private var gastoActual: Gasto? = null // Guardaremos aquí el gasto cargado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_gasto)

        // 1. Recibir el ID que nos manda el Listado
        val idGasto = intent.getIntExtra("ID_GASTO", -1)

        if (idGasto == -1) {
            finish() // Si falla el ID, cerramos
            return
        }

        // 2. Referencias a tu XML (activity_detalle_gasto.xml)
        val tvCantidad = findViewById<TextView>(R.id.tvDetalleCantidad)
        val tvConcepto = findViewById<TextView>(R.id.tvDetalleConcepto)
        val tvCategoria = findViewById<TextView>(R.id.tvDetalleCategoria)
        val tvFecha = findViewById<TextView>(R.id.tvDetalleFecha)
        val btnEliminar = findViewById<MaterialButton>(R.id.btnEliminar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        db = GastosDatabase.getDatabase(this)

        // 3. CARGAR DATOS (Corrutina)
        lifecycleScope.launch {
            // A. Buscamos el Gasto por su ID
            val gasto = db.gastoDao().obtenerGastoPorId(idGasto)

            if (gasto != null) {
                gastoActual = gasto // Lo guardamos por si hay que borrarlo luego

                // B. Rellenamos la UI con los datos del Gasto
                tvCantidad.text = "${String.format("%.2f", gasto.cantidad)} €"
                tvConcepto.text = gasto.concepto
                tvFecha.text = "${gasto.fecha} - ${gasto.hora}"

                // C. TRUCO: Buscamos el nombre de la Categoría usando el ID
                if (gasto.categoriaId != null) {
                    val categoria = db.categoriaDao().obtenerCategoriaPorId(gasto.categoriaId)
                    tvCategoria.text = categoria?.nombre ?: "Sin Categoría"
                } else {
                    tvCategoria.text = "Sin Categoría"
                }

            } else {
                Toast.makeText(this@DetalleGastoActivity, "Error al cargar gasto", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // 4. BOTÓN ELIMINAR
        btnEliminar.setOnClickListener {
            gastoActual?.let { gasto ->
                lifecycleScope.launch {
                    db.gastoDao().borrarGasto(gasto)
                    Toast.makeText(this@DetalleGastoActivity, "Gasto eliminado", Toast.LENGTH_SHORT).show()
                    finish() // Volver al listado
                }
            }
        }

        // 5. BOTÓN VOLVER
        btnVolver.setOnClickListener {
            finish()
        }
    }
}