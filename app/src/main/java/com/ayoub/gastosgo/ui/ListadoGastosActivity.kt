package com.ayoub.gastosgo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.GastosDatabase
import kotlinx.coroutines.launch

class ListadoGastosActivity : AppCompatActivity() {

    private lateinit var usuarioActual: String
    private lateinit var listView: ListView
    private lateinit var tvVacia: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_gastos)

        // 1. Recuperar Usuario
        usuarioActual = intent.getStringExtra("USUARIO_ACTUAL") ?: return

        // 2. Referencias UI
        listView = findViewById(R.id.listViewGastos)
        tvVacia = findViewById(R.id.tvListaVacia)

        // 3. Cargar datos
        cargarLista()

        // 4. Configurar Clic en cada elemento para ir al Detalle
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Obtenemos el objeto Gasto seleccionado del adaptador
            val gastoSeleccionado = (listView.adapter as GastosAdapter).getItem(position)

            gastoSeleccionado?.let {
                val intent = Intent(this, DetalleGastoActivity::class.java)
                intent.putExtra("ID_GASTO", it.id) // Pasamos el ID para buscarlo luego
                startActivity(intent)
            }
        }
    }

    // Usamos onResume para que si borras un gasto en Detalle y vuelves, la lista se actualice
    override fun onResume() {
        super.onResume()
        cargarLista()
    }

    private fun cargarLista() {
        val db = GastosDatabase.getDatabase(this)

        lifecycleScope.launch {
            // Llamada al DAO
            val listaGastos = db.gastoDao().obtenerGastos(usuarioActual)

            if (listaGastos.isEmpty()) {
                listView.visibility = View.GONE
                tvVacia.visibility = View.VISIBLE
            } else {
                listView.visibility = View.VISIBLE
                tvVacia.visibility = View.GONE

                // Conectamos el Adaptador
                val adapter = GastosAdapter(this@ListadoGastosActivity, listaGastos)
                listView.adapter = adapter
            }
        }
    }
}