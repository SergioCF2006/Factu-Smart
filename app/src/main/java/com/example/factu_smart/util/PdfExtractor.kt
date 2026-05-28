
package com.example.factu_smart.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.factu_smart.data.model.Factura
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream

object PdfExtractor {

    @SuppressLint("DefaultLocale")
    fun extraerInformacion(
        context: Context,
        uri: Uri
    ): Factura? {

        try {

            PDFBoxResourceLoader.init(context)

        } catch (e: Exception) {

            Log.e(
                "PdfExtractor",
                e.message ?: ""
            )
        }

        var document: PDDocument? = null

        return try {

            val inputStream: InputStream? =
                context.contentResolver
                    .openInputStream(uri)

            if (inputStream == null) {
                return null
            }

            document =
                PDDocument.load(inputStream)

            val stripper =
                PDFTextStripper()

            stripper.sortByPosition = true

            val text =
                stripper.getText(document)

            Log.d(
                "PDF_TEXT",
                text
            )


            val nitEmisor =
                Regex(
                    "Nit\\s+Emisor:\\s*([\\d-]+)",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)

            val nitReceptor =
                Regex(
                    "NIT\\s+Receptor:\\s*([\\d-]+)",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)


            val nombreEmisor =
                Regex(
                    "(.*?)\\n\\s*Nit\\s+Emisor",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)
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
                    ?.groupValues?.getOrNull(1)
                    ?.trim()


            val montoTotal = try {

                val regexMonto =
                    Regex("(\\d+[\\.,]\\d{2})")

                regexMonto
                    .findAll(text)
                    .map {
                        it.value
                            .replace(",", "")
                    }
                    .lastOrNull()
                    ?.toDoubleOrNull()

                    ?: 0.0

            } catch (e: Exception) {

                0.0
            }


            var fechaBD = "2026-01-01"

            // Formato: 2026-05-28
            val fechaIso =
                Regex(
                    "(\\d{4})-(\\d{2})-(\\d{2})"
                )
                    .find(text)

            if (fechaIso != null) {

                fechaBD =
                    fechaIso.value

            } else {


                val fechaSlash =
                    Regex(
                        "(\\d{2})/(\\d{2})/(\\d{4})"
                    )
                        .find(text)

                if (fechaSlash != null) {

                    val dia =
                        fechaSlash.groupValues[1]

                    val mes =
                        fechaSlash.groupValues[2]

                    val anio =
                        fechaSlash.groupValues[3]

                    fechaBD =
                        "$anio-$mes-$dia"

                } else {


                    val fechaGuion =
                        Regex(
                            "(\\d{2})-(\\d{2})-(\\d{4})"
                        )
                            .find(text)

                    if (fechaGuion != null) {

                        val dia =
                            fechaGuion.groupValues[1]

                        val mes =
                            fechaGuion.groupValues[2]

                        val anio =
                            fechaGuion.groupValues[3]

                        fechaBD =
                            "$anio-$mes-$dia"
                    }
                }
            }


            val autorizacion =
                Regex(
                    "AUTORIZACIÓN:\\s*([A-Z0-9-]+)",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)


            val serie =
                Regex(
                    "Serie(?:\\s+del\\s+documento)?[:\\s]*([A-Z0-9]+)",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)


            val numero =
                Regex(
                    "(?:DTE|Número\\s+de\\s+Documento)[:\\s]*(\\d+)",
                    RegexOption.IGNORE_CASE
                )
                    .find(text)
                    ?.groupValues?.getOrNull(1)

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
                    String.format(
                        "%.2f",
                        montoTotal
                    ).toDouble(),

                monto_iva =
                    String.format(
                        "%.2f",
                        montoTotal * 0.12
                    ).toDouble(),

                descripcion =
                    "Factura FEL"
            )

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
