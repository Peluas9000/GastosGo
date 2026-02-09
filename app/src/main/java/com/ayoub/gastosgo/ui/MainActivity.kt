package com.example.gastosgo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.example.gastosgo.data.GastosDatabase
import com.example.gastosgo.ui.ListadoGastosActivity
import com.example.gastosgo.ui.NuevoGastoActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var usuarioActual: String
    private lateinit var tvResumen: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuarioActual = intent.getStringExtra("USUARIO_ACTUAL") ?: "Invitado"

        // Configurar saludo
        findViewById<TextView>(R.id.tvBienvenida).text = "Hola, $usuarioActual"
        tvResumen = findViewById(R.id.tvResumen)

    }
}