package com.example.factu_smart.ui.screen.listado

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
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

                        Toast.makeText(
                            context,
                            "No hay PDF guardado",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val uri = Uri.parse(ruta)

                    val intent = Intent(Intent.ACTION_VIEW).apply {

                        setDataAndType(
                            uri,
                            "application/pdf"
                        )

                        addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                    }

                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            "Abrir PDF"
                        )
                    )

                } catch (e: Exception) {

                    e.printStackTrace()

                    Toast.makeText(
                        context,
                        "Error al abrir PDF",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        ) {

            Text("VER PDF ORIGINAL")

        }

    }

}