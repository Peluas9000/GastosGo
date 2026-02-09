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

    }
}