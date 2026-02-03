package com.example.gastosgo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ayoub.gastosgo.R

import com.example.gastosgo.data.Gasto

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
        tvCantidad.text = "${String.format("%.2f", gasto.cantidad)} €"

        // Truco visual: Poner el precio en rojo si es muy alto
        if (gasto.cantidad > 100) {
            tvCantidad.setTextColor(ContextCompat.getColor(mContext, R.color.colorGasto))
        } else {
            tvCantidad.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor))
        }

        return layout
    }
}