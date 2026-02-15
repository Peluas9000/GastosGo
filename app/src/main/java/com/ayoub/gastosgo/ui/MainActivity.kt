package com.ayoub.gastosgo.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.GastosDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var usuarioActual: String
    private lateinit var tvResumen: TextView
    private lateinit var graficoPastel: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Recuperar Usuario
        usuarioActual = intent.getStringExtra("USUARIO_ACTUAL") ?: "Invitado"

        // 2. Referencias
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        tvResumen = findViewById(R.id.tvResumen) // Asegúrate que tu TextView del dinero tiene este ID
        val fabNuevo = findViewById<MaterialButton>(R.id.btnNuevoGasto) // O Button normal
        // 2. Vincular gráfico
        graficoPastel = findViewById(R.id.graficoPastel)

        tvBienvenida.text = "Hola, $usuarioActual"

        // 3. Botón Nuevo Gasto
        fabNuevo.setOnClickListener {
            val intent = Intent(this, NuevoGastoActivity::class.java)
            // ¡IMPORTANTE! Pasamos el usuario para que el gasto se guarde a su nombre
            intent.putExtra("USUARIO_ACTUAL", usuarioActual)
            startActivity(intent)
        }

        val btnHistorial = findViewById<MaterialButton>(R.id.btnVerHistorial)

        btnHistorial.setOnClickListener {
            val intent = Intent(this, ListadoGastosActivity::class.java)
            intent.putExtra("USUARIO_ACTUAL", usuarioActual)
            startActivity(intent)
        }

        cargarDatosGrafico()
    }

    // 4. ACTUALIZAR SALDO AL VOLVER
    // Este método se ejecuta siempre que la pantalla se muestra (al abrir o al volver de NuevoGasto)
    override fun onResume() {
        super.onResume()
        calcularSaldo()
        cargarDatosGrafico() // 3. Cargar datos al volver
    }

    private fun configurarGrafico() {
        graficoPastel.description.isEnabled = false // Quitar texto descriptivo pequeño
        graficoPastel.isDrawHoleEnabled = true // Hacerlo tipo "Donut"
        graficoPastel.setHoleColor(Color.TRANSPARENT)
        graficoPastel.setEntryLabelColor(Color.BLACK) // Color de las letras dentro del pastel
        graficoPastel.setEntryLabelTextSize(12f)
        graficoPastel.centerText = "Gastos"
        graficoPastel.setCenterTextSize(18f)
        graficoPastel.legend.isEnabled = false // Ocultamos leyenda si molesta
    }

    private fun cargarDatosGrafico() {
        val db = GastosDatabase.getDatabase(this)
        lifecycleScope.launch {
            // Usamos la nueva query del DAO
            val listaAgrupada = db.gastoDao().obtenerGastosPorCategoria(usuarioActual)

            // Convertimos nuestros datos a "Entradas" del gráfico
            val entradas = ArrayList<PieEntry>()

            for (item in listaAgrupada) {
                // (Valor numérico, Nombre Categoría)
                entradas.add(PieEntry(item.totalCantidad.toFloat(), item.categoriaNombre))
            }

            if (entradas.isNotEmpty()) {
                val dataSet = PieDataSet(entradas, "Categorías")

                // Colores bonitos automáticos (Material Design)
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextSize = 14f
                dataSet.valueTextColor = Color.WHITE

                val data = PieData(dataSet)
                graficoPastel.data = data
                graficoPastel.invalidate() // Refrescar gráfico
                graficoPastel.animateY(1400) // Animación chula al aparecer
            } else {
                graficoPastel.clear() // Si no hay datos, limpiar
            }
        }
    }

        private fun calcularSaldo() {
            val db = GastosDatabase.getDatabase(this)

            lifecycleScope.launch {

                // 1. Obtener TOTAL GASTADO (Lo que ya tenías)
                val totalGastado = db.gastoDao().obtenerTotalGastado(usuarioActual) ?: 0.0

                // 2. Obtener SUELDO / PRESUPUESTO (Nuevo)
                // Si por algún error no tiene sueldo, asumimos 0
                val sueldoTotal = db.bancoDao().obtenerSueldo(usuarioActual) ?: 0.0

                // 3. LA RESTA: ¿Cuánto me queda?
                val saldoRestante = sueldoTotal - totalGastado

                // 4. Mostrar en pantalla
                tvResumen.text = "${String.format("%.2f", saldoRestante)} €"

                // 5. LÓGICA VISUAL DE ALERTA ⚠️
                val tvEstado = findViewById<TextView>(R.id.tvEstadoPresupuesto) // Asegúrate de tener este ID en el XML o usa otro TextView

                if (saldoRestante < 0) {
                    // NÚMEROS ROJOS
                    tvResumen.setTextColor(getColor(android.R.color.holo_red_dark))
                    tvEstado?.text = "¡CUIDADO! Has superado tu presupuesto"
                    tvEstado?.setTextColor(getColor(android.R.color.holo_red_dark))
                } else if (saldoRestante < 200) {
                    // AVISO NARANJA (Poco saldo)
                    tvResumen.setTextColor(getColor(android.R.color.holo_orange_dark))
                    tvEstado?.text = "Te queda poco saldo..."
                    tvEstado?.setTextColor(getColor(android.R.color.holo_orange_dark))
                } else {
                    // TODO VERDE (Correcto)
                    tvResumen.setTextColor(getColor(R.color.primaryColor)) // Tu color normal
                    tvEstado?.text = "Finanzas saludables"
                }
            }



    }
}