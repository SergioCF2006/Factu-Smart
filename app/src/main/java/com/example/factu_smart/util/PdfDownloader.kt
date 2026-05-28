package com.example.factu_smart.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object PdfDownloader {

    fun descargarPdf(
        context: Context,
        urlPdf: String,
        nombreArchivo: String
    ): String? {

        return try {

            val carpeta = File(
                context.getExternalFilesDir(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                "facturas_descargadas"
            )

            if (!carpeta.exists()) {
                carpeta.mkdirs()
            }

            val archivo = File(
                carpeta,
                "$nombreArchivo.pdf"
            )

            val url = URL(urlPdf)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(archivo)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            archivo.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}