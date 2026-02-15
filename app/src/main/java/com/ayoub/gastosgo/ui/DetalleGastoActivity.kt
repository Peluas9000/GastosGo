package com.ayoub.gastosgo.ui

import android.content.ContentValues
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.Gasto
import com.ayoub.gastosgo.data.GastosDatabase
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.io.IOException

class DetalleGastoActivity : AppCompatActivity() {

    private lateinit var db: GastosDatabase
    private var gastoActual: Gasto? = null
    // Variable para guardar el nombre de la categoría (para ponerlo en el PDF)
    private var nombreCategoria: String = "Varios"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_gasto)

        // 1. IDs
        val idGasto = intent.getIntExtra("ID_GASTO", -1)
        if (idGasto == -1) { finish(); return }

        // 2. Referencias UI
        val tvCantidad = findViewById<TextView>(R.id.tvDetalleCantidad)
        val tvConcepto = findViewById<TextView>(R.id.tvDetalleConcepto)
        val tvCategoria = findViewById<TextView>(R.id.tvDetalleCategoria)
        val tvFecha = findViewById<TextView>(R.id.tvDetalleFecha)
        val btnEliminar = findViewById<MaterialButton>(R.id.btnEliminar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // REFERENCIA AL NUEVO BOTÓN
        val btnFactura = findViewById<MaterialButton>(R.id.btnFactura)

        db = GastosDatabase.getDatabase(this)

        // 3. CARGAR DATOS
        lifecycleScope.launch {
            val gasto = db.gastoDao().obtenerGastoPorId(idGasto)

            if (gasto != null) {
                gastoActual = gasto
                tvCantidad.text = "${String.format("%.2f", gasto.cantidad)} €"
                tvConcepto.text = gasto.concepto
                tvFecha.text = "${gasto.fecha} - ${gasto.hora}"

                if (gasto.categoriaId != null) {
                    val cat = db.categoriaDao().obtenerCategoriaPorId(gasto.categoriaId)
                    nombreCategoria = cat?.nombre ?: "Sin Categoría"
                    tvCategoria.text = nombreCategoria
                } else {
                    tvCategoria.text = "Sin Categoría"
                }
            }
        }

        // 4. LISTENER BOTÓN FACTURA (NUEVO)
        btnFactura.setOnClickListener {
            gastoActual?.let { gasto ->
                generarPDF(gasto)
            }
        }

        // 5. BOTÓN ELIMINAR
        btnEliminar.setOnClickListener {
            gastoActual?.let { gasto ->
                lifecycleScope.launch {
                    db.gastoDao().borrarGasto(gasto)
                    Toast.makeText(this@DetalleGastoActivity, "Gasto eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        btnVolver.setOnClickListener { finish() }
    }

    // --- FUNCIÓN MÁGICA PARA CREAR EL PDF ---
    private fun generarPDF(gasto: Gasto) {
        // 1. Crear documento tamaño A4
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Tamaño A4 estándar
        val page = pdfDocument.startPage(pageInfo)

        // 2. Preparar el "Pincel" (Paint) para dibujar
        val canvas = page.canvas
        val paint = Paint()
        val tituloPaint = Paint()

        // Estilos
        tituloPaint.textSize = 24f
        tituloPaint.color = Color.BLACK
        tituloPaint.isFakeBoldText = true
        tituloPaint.textAlign = Paint.Align.CENTER

        paint.textSize = 14f
        paint.color = Color.DKGRAY

        // 3. DIBUJAR EN EL PDF (Coordenadas X, Y)
        // Título
        canvas.drawText("FACTURA SIMPLIFICADA", 297f, 80f, tituloPaint) // Centro X=297
        canvas.drawText("GastosGo App", 297f, 110f, paint)

        // Línea separadora
        val lineaPaint = Paint()
        lineaPaint.color = Color.LTGRAY
        lineaPaint.strokeWidth = 2f
        canvas.drawLine(50f, 140f, 545f, 140f, lineaPaint)

        // Datos del Gasto
        val inicioY = 200f
        val saltoLinea = 40f

        paint.color = Color.BLACK
        paint.textSize = 16f

        canvas.drawText("Concepto:", 60f, inicioY, paint)
        canvas.drawText(gasto.concepto, 200f, inicioY, paint)

        canvas.drawText("Importe:", 60f, inicioY + saltoLinea, paint)
        // Pincel verde para el dinero
        val dineroPaint = Paint()
        dineroPaint.textSize = 16f
        dineroPaint.color = Color.parseColor("#388E3C") // Verde
        dineroPaint.isFakeBoldText = true
        canvas.drawText("${gasto.cantidad} €", 200f, inicioY + saltoLinea, dineroPaint)

        canvas.drawText("Fecha:", 60f, inicioY + saltoLinea * 2, paint)
        canvas.drawText("${gasto.fecha} a las ${gasto.hora}", 200f, inicioY + saltoLinea * 2, paint)

        canvas.drawText("Categoría:", 60f, inicioY + saltoLinea * 3, paint)
        canvas.drawText(nombreCategoria, 200f, inicioY + saltoLinea * 3, paint)

        canvas.drawText("Usuario:", 60f, inicioY + saltoLinea * 4, paint)
        canvas.drawText(gasto.usuarioAutor, 200f, inicioY + saltoLinea * 4, paint)

        // Pie de página
        paint.textSize = 12f
        paint.color = Color.GRAY
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Generado automáticamente por GastosGo", 297f, 800f, paint)

        // 4. Finalizar página
        pdfDocument.finishPage(page)

        // 5. GUARDAR EL ARCHIVO (Compatible con Android 10+ Scoped Storage)
        val nombreArchivo = "Factura_${gasto.id}_${System.currentTimeMillis()}.pdf"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // MÉTODO MODERNO (Android 10+) -> Usa MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    val outputStream = contentResolver.openOutputStream(it)
                    if (outputStream != null) {
                        pdfDocument.writeTo(outputStream)
                        outputStream.close()
                        Toast.makeText(this, "PDF Guardado en Descargas 📥", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // MÉTODO ANTIGUO (Para móviles viejos, requiere permisos, pero en emulador moderno usará el de arriba)
                // Nota: Si usas un móvil muy viejo real, necesitarías pedir permiso WRITE_EXTERNAL_STORAGE
                Toast.makeText(this, "Versión de Android antigua, actualización necesaria", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}