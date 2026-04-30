package com.example.factu_smart.ui.screen.listado

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.factu_smart.util.PdfExtractor
import com.example.factu_smart.data.remote.ClienteApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SubirFacturasScreen(
    onInicio: () -> Unit,
    onBuscar: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }
    
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
                        val facturaExtraida = withContext(Dispatchers.IO) {
                            PdfExtractor.extraerInformacion(context, uri)
                        }
                        
                        if (facturaExtraida != null) {
                            try {
                                val response = ClienteApi.servicio.guardarFactura(facturaExtraida)
                                if (response.isSuccessful) {
                                    exitos++
                                } else {
                                    errores++
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error API ${response.code()}: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: Exception) {
                                errores++
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            errores++
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "No se pudo extraer info del PDF", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    
                    isUploading = false
                    Toast.makeText(context, "Finalizado: $exitos éxito, $errores error", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(onInicio = onInicio, onIngreso = {}, onBuscar = onBuscar)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFBCBCBC))
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Menu, null, modifier = Modifier.size(40.dp), tint = Color.Gray)
                Column(horizontalAlignment = Alignment.End) {
                    Icon(Icons.Default.Description, null, modifier = Modifier.size(30.dp))
                    Text("Factu-Smart", fontWeight = FontWeight.Bold)
                    Text("GESTIÓN DE FACTURAS ELECTRÓNICAS", fontSize = 8.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("SUBIR FACTURAS", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(modifier = Modifier.height(30.dp))

            Icon(
                imageVector = if (isUploading) Icons.Default.Sync else Icons.Default.FileUpload,
                contentDescription = null,
                modifier = Modifier.size(180.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (isUploading) "Procesando..." else "Seleccione sus facturas",
                fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { launcher.launch(arrayOf("application/pdf")) },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isUploading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF4A69A7))
                else Text("Subir", color = Color(0xFF4A69A7), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedFiles.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumen de Carga", fontWeight = FontWeight.Bold, color = Color.Gray)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Archivos: ${selectedFiles.size}", color = Color.DarkGray)
                            if (!isUploading) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50))
                        }
                    }
                }
            }
        }
    }
}
