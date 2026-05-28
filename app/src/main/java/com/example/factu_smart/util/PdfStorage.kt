package com.example.factu_smart.util

import android.content.Context
import android.net.Uri
import java.io.File

object PdfStorage {

    fun guardarPdf(

        context: Context,
        uri: Uri

    ): String? {

        return try {

            val carpeta = File(

                context.filesDir,

                "facturas"

            )

            if (

                !carpeta.exists()

            ) {

                carpeta.mkdirs()

            }

            val nombre =

                "FACTURA_${
                    System.currentTimeMillis()
                }.pdf"

            val archivo = File(

                carpeta,

                nombre

            )

            context.contentResolver
                .openInputStream(uri)
                ?.use { entrada ->

                    archivo.outputStream()
                        .use {

                                salida ->

                            entrada.copyTo(
                                salida
                            )

                        }

                }

            archivo.absolutePath

        } catch (

            e: Exception

        ) {

            e.printStackTrace()

            null

        }

    }

}