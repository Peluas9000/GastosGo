package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.Categoria
import com.ayoub.gastosgo.data.Gasto
import com.ayoub.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NuevoGastoActivity : AppCompatActivity() {

    // Variables globales para acceder desde el botón
    private lateinit var spinner: Spinner
    private var listaCategorias: List<Categoria> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_gasto)

        // 1. RECUPERAR EL USUARIO (Vital para saber de quién es el gasto)
        val usuarioActual = intent.getStringExtra("USUARIO_ACTUAL")

        // Seguridad: Si no hay usuario, cerramos para evitar errores
        if (usuarioActual == null) {
            Toast.makeText(this, "Error: No hay usuario identificado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. VINCULAR CON EL XML
        val etCantidad = findViewById<TextInputEditText>(R.id.etCantidad)
        val etConcepto = findViewById<TextInputEditText>(R.id.etConcepto)
        spinner = findViewById(R.id.spinnerCategorias) // <--- El Spinner que añadimos al XML
        val tvFecha = findViewById<TextView>(R.id.tvFechaHoraActual)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardar)
        val btnCancelar = findViewById<MaterialButton>(R.id.btnCancelar)

        // 3. BASE DE DATOS
        val db = GastosDatabase.getDatabase(this)

        // Poner la fecha de hoy automáticamente
        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val horaHoy = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        tvFecha.text = "$fechaHoy - $horaHoy"

        // 4. CARGAR CATEGORÍAS EN EL SPINNER (Paso previo obligatorio)
        lifecycleScope.launch {
            // Pedimos las categorías a la BD
            listaCategorias = db.categoriaDao().obtenerTodas()

            // Sacamos solo los nombres (String) para mostrarlos en la lista
            val nombresCategorias = listaCategorias.map { it.nombre }

            // Creamos el adaptador visual para el Spinner
            val adapter = ArrayAdapter(
                this@NuevoGastoActivity,
                android.R.layout.simple_spinner_item,
                nombresCategorias
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // ---------------------------------------------------------
        // 5. LÓGICA DEL BOTÓN GUARDAR (Aquí está lo que pedías)
        // ---------------------------------------------------------
        btnGuardar.setOnClickListener {
            val cantidadTexto = etCantidad.text.toString()
            val concepto = etConcepto.text.toString()

            // Validaciones: Que no esté vacío y que haya categorías cargadas
            if (cantidadTexto.isNotEmpty() && concepto.isNotEmpty() && listaCategorias.isNotEmpty()) {

                // A. Convertir cantidad a número
                val cantidad = cantidadTexto.toDouble()

                // B. Obtener el ID de la categoría seleccionada
                // (El spinner nos da la posición 0, 1, 2... usamos esa posición para buscar en nuestra lista)
                val posicion = spinner.selectedItemPosition
                val categoriaIdSeleccionada = listaCategorias[posicion].id

                // C. Insertar en Base de Datos (Corrutina)
                lifecycleScope.launch {
                    val nuevoGasto = Gasto(
                        usuarioAutor = usuarioActual, // El usuario que entró en Login
                        categoriaId = categoriaIdSeleccionada,
                        concepto = concepto,
                        fecha = fechaHoy,
                        hora = horaHoy,
                        cantidad = cantidad
                    )

                    db.gastoDao().insertarGasto(nuevoGasto)

                    // D. Feedback y Salir
                    Toast.makeText(this@NuevoGastoActivity, "Gasto guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Cierra esta pantalla y vuelve al Main (donde se actualizará el saldo)
                }

            } else {
                Toast.makeText(this, "Rellena cantidad y concepto", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Cancelar
        btnCancelar.setOnClickListener {
            finish()
        }
    }
}