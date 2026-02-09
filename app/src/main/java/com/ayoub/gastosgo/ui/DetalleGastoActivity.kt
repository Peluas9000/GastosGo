package com.ayoub.gastosgo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.GastoDao
import com.example.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetalleGastoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_gasto)

        val idGasto = intent.getIntExtra("ID_GASTO", 0)
        val db = GastosDatabase.getDatabase(this)


    }

    private fun GastoDao.gastoPorId(idGasto: Int) {}
}

