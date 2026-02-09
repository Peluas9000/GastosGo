package com.example.gastosgo.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.example.gastosgo.data.Categoria
import com.example.gastosgo.data.Gasto
import com.example.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NuevoGastoActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private var listaCategorias: List<Categoria> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_gasto)

        val usuarioActual = intent.getStringExtra("USUARIO_ACTUAL") ?: ""
        val db = GastosDatabase.getDatabase(this)

        val etCantidad = findViewById<TextInputEditText>(R.id.etCantidad)
        val etConcepto = findViewById<TextInputEditText>(R.id.etConcepto)
        spinner = findViewById(R.id.etCategoria) // En el XML cambiamos el EditText por un Spinner, o usamos un Spinner directamente

        // Poner fecha actual
        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.tvFechaHoraActual).text = fechaHoy


    }
}