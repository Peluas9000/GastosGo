package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.Banco
import com.ayoub.gastosgo.data.GastosDatabase
import com.ayoub.gastosgo.data.Usuario
import com.ayoub.gastosgo.utils.Cifrado
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
        val btnGuardar = findViewById<MaterialButton>(R.id.btnCrearCuenta)

        val db = GastosDatabase.getDatabase(this)

        btnGuardar.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()
            val confirm = etConfirm.text.toString().trim()

            // VALIDACIÓN 1: Campos vacíos
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Debes rellenar usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // VALIDACIÓN 2: Contraseñas coinciden
            if (pass != confirm) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // INSERTAR EN BASE DE DATOS
            lifecycleScope.launch {
                try {
                    val passCifrada = Cifrado.cifrar(pass)
                    val nuevoUsuario = Usuario(user, passCifrada)
                    db.usuarioDao().registrarUsuario(nuevoUsuario)

                    // --- NUEVO CÓDIGO: ASIGNAR SALDO INICIAL ---
                    // Le damos un saldo por defecto (ej: 2500.00) vinculado a su nombre
                    val miBanco = Banco(usuarioTitular = user, saldoMensual = 2500.0)
                    db.bancoDao().insertarSaldo(miBanco)

                    // Si llega aquí, es que se guardó bien
                    Toast.makeText(this@RegistrarActivity, "¡Registro completado!", Toast.LENGTH_SHORT).show()
                    finish() // Vuelve automáticamente a la pantalla anterior (Login)

                } catch (e: Exception) {
                    // Si el usuario ya existe (Primary Key conflict), salta aquí
                    Toast.makeText(this@RegistrarActivity, "Error: El usuario '$user' ya existe", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Botón o Texto para "Volver" (Opcional, si tienes uno en el XML)
        findViewById<android.view.View>(R.id.tvVolverLogin)?.setOnClickListener {
            finish()
        }
    }
}