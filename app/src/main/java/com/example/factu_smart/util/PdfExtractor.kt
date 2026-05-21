package com.example.factu_smart.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import com.example.factu_smart.data.model.Factura
import java.io.InputStream

object PdfExtractor {

    fun extraerInformacion(
        context: Context,
        uri: Uri
    ): Factura? {

        try {

            PDFBoxResourceLoader.init(context)

        } catch (e: Exception) {

            Log.e(
                "PdfExtractor",
                "Error inicializando PDFBox: ${e.message}"
            )

        }

        var document: PDDocument? = null

        return try {

            val inputStream: InputStream? =
                context.contentResolver
                    .openInputStream(uri)

            if (inputStream != null) {

                document =
                    PDDocument.load(inputStream)

                val stripper =
                    PDFTextStripper()

                stripper.sortByPosition =
                    true

                val text =
                    stripper.getText(document)

                val nitEmisor =
                    Regex(
                        "Nit\\s+Emisor:\\s*([\\d-]+)",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)

                val nitReceptor =
                    Regex(
                        "NIT\\s+Receptor:\\s*([\\d-]+)",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)

                val nombreEmisor =
                    Regex(
                        "(.*?)\\n\\s*Nit\\s+Emisor",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)
                        ?.trim()


                val nombreReceptor =
                    Regex(
                        "Nombre\\s+Receptor:\\s*(.*?)(Fecha y hora|NIT|$)",
                        setOf(
                            RegexOption.IGNORE_CASE,
                            RegexOption.DOT_MATCHES_ALL
                        )
                    )
                        .find(text)
                        ?.groupValues?.get(1)
                        ?.trim()

                val indexTotales =
                    text.indexOf(
                        "TOTALES:",
                        ignoreCase = true
                    )

                val montoTotal =
                    if (indexTotales != -1) {

                        val subText =
                            text.substring(indexTotales)
                                .take(120)

                        val montos =
                            Regex(
                                "([\\d,]+\\.\\d{2})"
                            )
                                .findAll(subText)
                                .map {
                                    it.value
                                }
                                .toList()

                        montos.lastOrNull()
                            ?.replace(",", "")
                            ?.toDoubleOrNull()
                            ?: 0.0

                    } else {

                        0.0

                    }

                val fechaRaw =
                    Regex(
                        "emision:\\s*(\\d{2})-([a-z]{3})-(\\d{4})",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)

                val fechaBD =
                    if (fechaRaw != null) {

                        val dia =
                            fechaRaw.groupValues[1]

                        val mesStr =
                            fechaRaw.groupValues[2]
                                .lowercase()

                        val anio =
                            fechaRaw.groupValues[3]

                        val mes =
                            when {

                                mesStr.contains("ene") -> "01"
                                mesStr.contains("feb") -> "02"
                                mesStr.contains("mar") -> "03"
                                mesStr.contains("abr") -> "04"
                                mesStr.contains("may") -> "05"
                                mesStr.contains("jun") -> "06"
                                mesStr.contains("jul") -> "07"
                                mesStr.contains("ago") -> "08"
                                mesStr.contains("sep") -> "09"
                                mesStr.contains("oct") -> "10"
                                mesStr.contains("nov") -> "11"
                                mesStr.contains("dic") -> "12"

                                else -> "01"

                            }

                        "$anio-$mes-$dia"

                    } else {

                        null

                    }

                val autorizacion =
                    Regex(
                        "AUTORIZACIÓN:\\s*([A-Z0-9-]+)",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)

                val serie =
                    Regex(
                        "Serie:\\s*([A-Z0-9]+)",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)

                val numero =
                    Regex(
                        "DTE:\\s*(\\d+)",
                        RegexOption.IGNORE_CASE
                    )
                        .find(text)
                        ?.groupValues?.get(1)

                Factura(

                    nit_emisor =
                        nitEmisor,

                    nombre_emisor =
                        nombreEmisor,

                    nit_receptor =
                        nitReceptor,

                    nombre_receptor =
                        nombreReceptor,

                    numero_autorizacion =
                        autorizacion,

                    serie =
                        serie,

                    numero_documento =
                        numero,

                    fecha_emision =
                        fechaBD,

                    monto_total =
                        montoTotal,

                    monto_iva =
                        montoTotal * 0.12,

                    descripcion =
                        "Factura FEL"

                )

            } else {

                null

            }

        } catch (e: Exception) {

            Log.e(
                "PdfExtractor",
                "Error: ${e.message}"
            )

            null

        } finally {

            document?.close()

        }

    }

}