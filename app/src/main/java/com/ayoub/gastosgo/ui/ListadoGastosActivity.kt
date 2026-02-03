package com.example.gastosgo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.ui.DetalleGastoActivity
import com.example.gastosgo.data.GastosDatabase
import kotlinx.coroutines.launch

class ListadoGastosActivity : AppCompatActivity() {

    private lateinit var db: GastosDatabase
    private lateinit var usuarioActual: String
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_gastos)

        usuarioActual = intent.getStringExtra("USUARIO_ACTUAL") ?: ""
        db = GastosDatabase.getDatabase(this)
        listView = findViewById(R.id.listViewGastos)

        cargarGastos(ordenarPorPrecio = false) // Por defecto orden normal

        // Clic en un elemento -> Ir a Detalle
        listView.setOnItemClickListener { _, _, position, _ ->
            val gastoSeleccionado = (listView.adapter as GastosAdapter).getItem(position)
            gastoSeleccionado?.let {
                val intent = Intent(this, DetalleGastoActivity::class.java)
                intent.putExtra("ID_GASTO", it.id)
                startActivity(intent)
            }
        }
    }

    // Función para cargar datos con opción de filtro
    private fun cargarGastos(ordenarPorPrecio: Boolean) {
        lifecycleScope.launch {
            val lista = if (ordenarPorPrecio) {
                db.gastoDao().obtenerGastosPorPrecio(usuarioActual)
            } else {
                db.gastoDao().obtenerGastos(usuarioActual)
            }

            if (lista.isEmpty()) {
                Toast.makeText(this@ListadoGastosActivity, "No hay movimientos", Toast.LENGTH_SHORT).show()
            }

            val adapter = GastosAdapter(this@ListadoGastosActivity, lista)
            listView.adapter = adapter
        }
    }

    // REQUISITO 4: Menú para activar el filtro
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu) // Necesitas crear res/menu/menu_listado.xml
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filtro_precio -> {
                cargarGastos(ordenarPorPrecio = true)
                Toast.makeText(this, "Ordenado por mayor gasto", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.filtro_defecto -> {
                cargarGastos(ordenarPorPrecio = false)
                Toast.makeText(this, "Ordenado por fecha", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Recargar al volver de borrar
    override fun onResume() {
        super.onResume()
        cargarGastos(false)
    }
}