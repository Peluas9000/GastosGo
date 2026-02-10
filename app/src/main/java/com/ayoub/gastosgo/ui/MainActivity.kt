package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ayoub.gastosgo.R

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