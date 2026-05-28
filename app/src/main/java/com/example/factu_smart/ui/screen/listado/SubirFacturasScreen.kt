<<<<<<< HEAD

package com.example.factu_smart.ui.screen.listado

=======
package com.example.factu_smart.ui.screen.listado

import android.net.Uri
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
<<<<<<< HEAD
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
=======
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.util.PdfExtractor
import com.example.factu_smart.util.PdfStorage
=======
import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.util.PdfExtractor
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SubirFacturasScreen(
    onInicio: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
<<<<<<< HEAD

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

=======
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedFiles by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
    var isUploading by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
<<<<<<< HEAD

=======
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
        onResult = { uris ->

            if (uris.isNotEmpty()) {

<<<<<<< HEAD
=======
                selectedFiles = uris

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                scope.launch {

                    isUploading = true

                    var exitos = 0
                    var errores = 0

                    uris.forEach { uri ->

<<<<<<< HEAD
                        try {

                            // 📄 Guardar PDF
                            val rutaPdf =
                                PdfStorage.guardarPdf(
=======
                        val facturaExtraida =
                            withContext(Dispatchers.IO) {

                                PdfExtractor.extraerInformacion(
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                                    context,
                                    uri
                                )

<<<<<<< HEAD
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


=======
                            }

                        if (facturaExtraida != null) {

                            try {
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd

                                val response =
                                    ClienteApi.servicio
                                        .guardarFactura(
<<<<<<< HEAD
                                            facturaFinal
                                        )


=======
                                            facturaExtraida
                                        )

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                                if (response.isSuccessful) {

                                    exitos++

<<<<<<< HEAD
                                    withContext(Dispatchers.Main) {

                                        Toast.makeText(
                                            context,
                                            "✅ Subida correcta",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

=======
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                                } else {

                                    errores++

<<<<<<< HEAD
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
=======
                                }

                            } catch (e: Exception) {

                                errores++

                            }

                        } else {

                            errores++

                        }

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                    }

                    isUploading = false

<<<<<<< HEAD
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
=======
                    Toast.makeText(
                        context,
                        "Finalizado: $exitos éxito, $errores error",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

        }

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
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
<<<<<<< HEAD
                        Text("Cerrar sesión")
=======

                        Text("Cerrar sesión")

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                    },

                    selected = false,

                    onClick = {
<<<<<<< HEAD
                        onCerrarSesion()
=======

                        onCerrarSesion()

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                    },

                    icon = {

                        Icon(
                            Icons.Default.ExitToApp,
<<<<<<< HEAD
                            contentDescription = null
                        )
                    }
                )
            }
        }
=======
                            null
                        )

                    }

                )

            }

        }

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
    ) {

        Scaffold(

            bottomBar = {

                BottomNavigationBar(
<<<<<<< HEAD
                    onInicio = onInicio,
                    onIngreso = {},
                    onBuscar = onBuscar
                )
=======

                    onInicio = onInicio,

                    onIngreso = {},

                    onBuscar = onBuscar

                )

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
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

<<<<<<< HEAD
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
=======
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.Top
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd

                ) {

                    IconButton(

                        onClick = {

                            scope.launch {
<<<<<<< HEAD
                                drawerState.open()
                            }
=======

                                drawerState.open()

                            }

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                        }

                    ) {

                        Icon(
<<<<<<< HEAD
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
=======

                            Icons.Default.Menu,

                            null,

                            modifier =
                                Modifier.size(40.dp),

                            tint =
                                Color.Gray

                        )

                    }

                    Column(

                        horizontalAlignment =
                            Alignment.End

                    ) {

                        Icon(

                            Icons.Default.Description,

                            null,

                            modifier =
                                Modifier.size(30.dp)

                        )

                        Text(

                            "Factu-Smart",

                            fontWeight =
                                FontWeight.Bold

                        )

                        Text(

                            "GESTIÓN DE FACTURAS ELECTRÓNICAS",

                            fontSize = 8.sp

                        )

                    }

                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(

                    "SUBIR FACTURAS",

                    fontSize = 28.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        Color.White

                )

                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )

                Icon(

                    imageVector =

                        if (isUploading)

                            Icons.Default.Sync

                        else

                            Icons.Default.FileUpload,

                    contentDescription = null,

                    modifier =
                        Modifier.size(180.dp),

                    tint =
                        Color.White

                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(

                    if (isUploading)

                        "Procesando..."

                    else

                        "Seleccione sus facturas",

                    color =
                        Color.White,

                    fontSize = 18.sp

                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                )

                Button(

                    onClick = {

                        launcher.launch(
<<<<<<< HEAD
                            arrayOf("application/pdf")
                        )
                    },

                    enabled = !isUploading,
=======

                            arrayOf(
                                "application/pdf"
                            )

                        )

                    },

                    enabled =
                        !isUploading,
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    colors =
                        ButtonDefaults.buttonColors(
<<<<<<< HEAD
                            containerColor = Color.White
=======

                            containerColor =
                                Color.White

>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
                        ),

                    shape =
                        RoundedCornerShape(8.dp)

                ) {

                    Text(

<<<<<<< HEAD
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

=======
                        "Subir",

                        color =
                            Color(0xFF4A69A7),

                        fontSize = 22.sp

                    )

                }

            }

        }

    }
}
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
