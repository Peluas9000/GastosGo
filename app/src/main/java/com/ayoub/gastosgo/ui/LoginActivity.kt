package com.ayoub.gastosgo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.example.gastosgo.data.GastosDatabase
import com.ayoub.gastosgo.utils.Cifrado
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etPass = findViewById<TextInputEditText>(R.id.etPass)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegistrar = findViewById<MaterialButton>(R.id.btnRegistrar)

        val db = GastosDatabase.getDatabase(this)

        btnLogin.setOnClickListener {
            val user = etUsuario.text.toString()
            val pass = etPass.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                lifecycleScope.launch {
                    // CIFRAMOS la contraseña para compararla con la BD
                    val passHash = Cifrado.hashSHA256(pass)
                    val usuarioEncontrado = db.usuarioDao().login(user, passHash)

                    if (usuarioEncontrado != null) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("USUARIO_ACTUAL", usuarioEncontrado.nombreUsuario)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }
}