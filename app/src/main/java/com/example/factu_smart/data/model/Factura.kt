
package com.example.factu_smart.data.model

data class Factura(

    val id: Int? = null,

    val nit_emisor: String? = null,
    val nombre_emisor: String? = null,

    val nit_receptor: String? = null,
    val nombre_receptor: String? = null,

    val numero_autorizacion: String? = null,
    val serie: String? = null,
    val numero_documento: String? = null,
    val fecha_emision: String? = null,

    val monto_total: Double? = 0.0,
    val monto_iva: Double? = 0.0,

    val descripcion: String? = null,

    val ruta_pdf: String? = null
)
