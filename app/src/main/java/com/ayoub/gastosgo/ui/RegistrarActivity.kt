package com.example.gastosgo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.example.gastosgo.data.GastosDatabase
import com.example.gastosgo.data.Usuario
import com.example.gastosgo.utils.Cifrado
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val etUser = findViewById<TextInputEditText>(R.id.etNuevoUsuario)
        val etPass = findViewById<TextInputEditText>(R.id.etNuevaPass)
        val etConfirm = findViewById<TextInputEditText>(R.id.etConfirmarPass)
        val btnCrear = findViewById<MaterialButton>(R.id.btnCrearCuenta)

        val db = GastosDatabase.getDatabase(this)



    }
}