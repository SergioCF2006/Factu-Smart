package com.example.factu_smart.ui.screen.listado

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.util.PdfExtractor
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

    var selectedFiles by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

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

                selectedFiles = uris

                scope.launch {

                    isUploading = true

                    var exitos = 0
                    var errores = 0

                    uris.forEach { uri ->

                        val facturaExtraida =
                            withContext(Dispatchers.IO) {

                                PdfExtractor.extraerInformacion(
                                    context,
                                    uri
                                )

                            }

                        if (facturaExtraida != null) {

                            try {

                                val response =
                                    ClienteApi.servicio
                                        .guardarFactura(
                                            facturaExtraida
                                        )

                                if (response.isSuccessful) {

                                    exitos++

                                } else {

                                    errores++

                                }

                            } catch (e: Exception) {

                                errores++

                            }

                        } else {

                            errores++

                        }

                    }

                    isUploading = false

                    Toast.makeText(

                        context,

                        "Finalizado: $exitos éxito, $errores error",

                        Toast.LENGTH_LONG

                    ).show()

                }

            }

        }

    )

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(

                    "Factu-Smart",

                    modifier =
                        Modifier.padding(16.dp),

                    fontWeight =
                        FontWeight.Bold

                )

                HorizontalDivider()

                NavigationDrawerItem(

                    label = {

                        Text(
                            "Cerrar sesión"
                        )

                    },

                    selected = false,

                    onClick = {

                        onCerrarSesion()

                    },

                    icon = {

                        Icon(
                            Icons.Default.ExitToApp,
                            null
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

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.Top

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
                )

                Button(

                    onClick = {

                        launcher.launch(
                            arrayOf(
                                "application/pdf"
                            )
                        )

                    },

                    enabled =
                        !isUploading,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color.White

                        ),

                    shape =
                        RoundedCornerShape(8.dp)

                ) {

                    Text(

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