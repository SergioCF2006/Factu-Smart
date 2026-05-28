package com.example.factu_smart.ui.screen.listado

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

import com.example.factu_smart.data.model.Factura

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import java.io.ByteArrayOutputStream

@Composable
fun PantallaDetalleFactura(

    factura: Factura,
    onVolver: () -> Unit

) {

    val context = LocalContext.current
    val view = LocalView.current

    PDFBoxResourceLoader.init(context)

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .statusBarsPadding()
            .padding(16.dp)

    ) {

        IconButton(

            onClick = {

                onVolver()

            }

        ) {

            Icon(

                Icons.Default.ArrowBack,

                contentDescription =
                    "Volver"

            )

        }

        Spacer(
            Modifier.height(20.dp)
        )

        Text(

            "Detalle Factura",

            style =
                MaterialTheme
                    .typography
                    .headlineSmall

        )

        Spacer(
            Modifier.height(20.dp)
        )

        Card(

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Column(

                Modifier.padding(20.dp)

            ) {

                Text(
                    "Cliente: ${factura.nombre_emisor}"
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                Text(
                    "NIT: ${factura.nit_emisor}"
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                Text(
                    "Total: Q${factura.monto_total}"
                )

            }

        }

        Spacer(
            Modifier.height(25.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                try {

                    val ruta = factura.ruta_pdf

                    if (ruta.isNullOrEmpty()) {
                        Toast.makeText(context, "No hay PDF guardado", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val file = java.io.File(ruta)

                    if (!file.exists()) {
                        Toast.makeText(context, "Archivo no encontrado", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        file
                    )

                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    context.startActivity(
                        android.content.Intent.createChooser(intent, "Abrir PDF")
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error al abrir PDF", Toast.LENGTH_SHORT).show()
                }

            }
        ) {
            Text("VER PDF ORIGINAL")
        }

    }

}

fun generarPDF(

    context: Context,
    view: android.view.View

) {

    try {

        val bitmap = Bitmap.createBitmap(

            view.width,
            view.height,

            Bitmap.Config.ARGB_8888

        )

        val canvas = Canvas(bitmap)

        view.draw(canvas)

        val documento = PDDocument()

        val pagina = PDPage(

            PDRectangle.A4

        )

        documento.addPage(

            pagina

        )

        val streamBitmap =

            ByteArrayOutputStream()

        bitmap.compress(

            Bitmap.CompressFormat.JPEG,

            100,

            streamBitmap

        )

        val imagen =

            JPEGFactory.createFromStream(

                documento,

                java.io.ByteArrayInputStream(

                    streamBitmap.toByteArray()

                )

            )

        val contenido =

            PDPageContentStream(

                documento,

                pagina

            )

        contenido.drawImage(

            imagen,

            20f,
            300f,

            550f,
            450f

        )

        contenido.close()

        val nombre =

            "Factura_${
                System.currentTimeMillis()
            }.pdf"

        val values = ContentValues()

        values.put(

            MediaStore.MediaColumns.DISPLAY_NAME,

            nombre

        )

        values.put(

            MediaStore.MediaColumns.MIME_TYPE,

            "application/pdf"

        )

        values.put(

            MediaStore.MediaColumns.RELATIVE_PATH,

            Environment.DIRECTORY_DOWNLOADS

        )

        val uri =

            context.contentResolver.insert(

                MediaStore.Files
                    .getContentUri(
                        "external"
                    ),

                values

            )

        uri?.let {

            context
                .contentResolver
                .openOutputStream(it)
                ?.use { output ->

                    documento.save(output)

                }

        }

        documento.close()

    } catch (

        e: Exception

    ) {

        e.printStackTrace()

    }

}