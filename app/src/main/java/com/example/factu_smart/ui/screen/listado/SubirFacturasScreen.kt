
package com.example.factu_smart.ui.screen.listado

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.util.PdfExtractor
import com.example.factu_smart.util.PdfStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SubirFacturasScreen(
    onInicio: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isUploading by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),

        onResult = { uris ->

            if (uris.isNotEmpty()) {

                scope.launch {

                    isUploading = true

                    var exitos = 0
                    var errores = 0

                    uris.forEach { uri ->

                        try {

                            // 📄 Guardar PDF
                            val rutaPdf =
                                PdfStorage.guardarPdf(
                                    context,
                                    uri
                                )

                            // 🧠 Extraer datos
                            val facturaExtraida =
                                withContext(Dispatchers.IO) {

                                    PdfExtractor.extraerInformacion(
                                        context,
                                        uri
                                    )
                                }

                            if (facturaExtraida == null) {

                                errores++
                                return@forEach
                            }

                            // 🗂️ Factura final
                            val facturaFinal =
                                facturaExtraida.copy(
                                    ruta_pdf = rutaPdf
                                )

                            // 🔍 Validar datos mínimos
                            if (
                                facturaFinal.serie.isNullOrBlank() ||
                                facturaFinal.numero_documento.isNullOrBlank()
                            ) {

                                errores++
                                return@forEach
                            }

                            // 🔍 Verificar duplicado
                            val duplicada =
                                ClienteApi.servicio
                                    .verificarFacturaDuplicada(

                                        "eq.${facturaFinal.serie}",

                                        "eq.${facturaFinal.numero_documento}"
                                    )

                            if (duplicada.isNotEmpty()) {

                                errores++

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "⚠️ Factura duplicada",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            } else {



                                val response =
                                    ClienteApi.servicio
                                        .guardarFactura(
                                            facturaFinal
                                        )


                                if (response.isSuccessful) {

                                    exitos++

                                    withContext(Dispatchers.Main) {

                                        Toast.makeText(
                                            context,
                                            "✅ Subida correcta",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } else {

                                    errores++

                                    val codigo =
                                        response.code()

                                    val error =
                                        response.errorBody()
                                            ?.string()

                                    withContext(Dispatchers.Main) {

                                        Toast.makeText(
                                            context,
                                            "ERROR $codigo -> $error",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                        } catch (e: Exception) {

                            errores++

                            withContext(Dispatchers.Main) {

                                Toast.makeText(
                                    context,
                                    "❌ ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    isUploading = false

                    withContext(Dispatchers.Main) {

                        Toast.makeText(
                            context,
                            "Finalizado: $exitos éxito, $errores error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    )

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    "Factu-Smart",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                NavigationDrawerItem(

                    label = {
                        Text("Cerrar sesión")
                    },

                    selected = false,

                    onClick = {
                        onCerrarSesion()
                    },

                    icon = {

                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) {

        Scaffold(

            bottomBar = {

                BottomNavigationBar(
                    onInicio = onInicio,
                    onIngreso = {},
                    onBuscar = onBuscar
                )
            }

        ) { padding ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFBCBCBC))
                    .padding(padding)
                    .padding(16.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    IconButton(

                        onClick = {

                            scope.launch {
                                drawerState.open()
                            }
                        }

                    ) {

                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    Column(
                        horizontalAlignment =
                            Alignment.End
                    ) {

                        Icon(
                            Icons.Default.Description,
                            contentDescription = null
                        )

                        Text(
                            "Factu-Smart",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "GESTIÓN DE FACTURAS",
                            fontSize = 8.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    "SUBIR FACTURAS",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = null,
                    modifier = Modifier.size(160.dp),
                    tint = Color.White
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(

                    onClick = {

                        launcher.launch(
                            arrayOf("application/pdf")
                        )
                    },

                    enabled = !isUploading,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),

                    shape =
                        RoundedCornerShape(8.dp)

                ) {

                    Text(

                        if (isUploading)
                            "Procesando..."
                        else
                            "Subir Facturas",

                        color = Color(0xFF4A69A7),

                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

