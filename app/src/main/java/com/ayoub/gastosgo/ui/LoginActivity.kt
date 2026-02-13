package com.ayoub.gastosgo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Referencias a la vista
        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etPass = findViewById<TextInputEditText>(R.id.etPass)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnIrRegistro = findViewById<MaterialButton>(R.id.btnRegistrar)

        // 2. Instancia de la Base de Datos
        val db = GastosDatabase.getDatabase(this)

        // --- BOTÓN INICIAR SESIÓN ---
        btnLogin.setOnClickListener {
            val usuarioInput = etUsuario.text.toString().trim() // .trim() quita espacios al final
            val passInput = etPass.text.toString().trim()

            if (usuarioInput.isEmpty() || passInput.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Lanzamos la Corrutina (Segundo Plano)
                lifecycleScope.launch {
                    // CONSULTA: ¿Existe este usuario con esta contraseña?
                    val usuarioEncontrado = db.usuarioDao().login(usuarioInput, passInput)

                    if (usuarioEncontrado != null) {
                        // ¡ÉXITO! -> Vamos al Main
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("USUARIO_ACTUAL", usuarioEncontrado.nombreUsuario)
                        startActivity(intent)
                        finish() // Cerramos Login para que no pueda volver atrás con el botón 'Atrás'
                    } else {
                        // ERROR -> Usuario o contraseña mal
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // --- BOTÓN IR A REGISTRO ---
        btnIrRegistro.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }
    }
}