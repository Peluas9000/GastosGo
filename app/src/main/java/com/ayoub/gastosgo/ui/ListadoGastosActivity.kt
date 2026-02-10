package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.ayoub.gastosgo.R
import com.example.gastosgo.data.GastosDatabase

class ListadoGastosActivity : AppCompatActivity() {

    private lateinit var db: GastosDatabase
    private lateinit var usuarioActual: String
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_gastos)

    }
}