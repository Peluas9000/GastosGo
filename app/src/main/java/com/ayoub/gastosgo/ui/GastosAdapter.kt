package com.ayoub.gastosgo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ayoub.gastosgo.R
import com.ayoub.gastosgo.data.Gasto


class GastosAdapter(
    private val mContext: Context,
    private val listaGastos: List<Gasto>
) : ArrayAdapter<Gasto>(mContext, 0, listaGastos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // 1. Inflar el layout personalizado si no existe
        val layout = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_gasto, parent, false)

        // 2. Obtener el gasto actual
        val gasto = listaGastos[position]

        // 3. Referencias a los TextViews del diseño item_gasto.xml
        val tvConcepto = layout.findViewById<TextView>(R.id.tvConceptoItem)
        val tvFecha = layout.findViewById<TextView>(R.id.tvFechaItem)
        val tvCantidad = layout.findViewById<TextView>(R.id.tvCantidadItem)

        // 4. Asignar datos
        tvConcepto.text = gasto.concepto
        tvFecha.text = gasto.fecha
        // CAMBIO 1: Añadimos el signo "-" manualmente al texto
        tvCantidad.text = "- ${String.format("%.2f", gasto.cantidad)} €"

        // CAMBIO 2: Forzamos el color ROJO para todos (porque son gastos)
        // Usamos 'holo_red_dark' que ya viene en Android, o tu color personalizado si lo prefieres
        tvCantidad.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark))


        //


        return layout
    }
}