package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi

@Composable
fun PantallaListado(
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit
) {
    var lista by remember { mutableStateOf<List<Factura>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            lista = ClienteApi.servicio.obtenerFacturas()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onInicio = onInicio,
                onIngreso = onIngreso,
                onBuscar = onBuscar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9))
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Facturas Guardadas", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lista) { factura ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Cliente: ${factura.nombre_emisor}", style = MaterialTheme.typography.bodyLarge)
                            Text("NIT: ${factura.nit_emisor}", color = Color.Gray)
                            Text("Total: Q${factura.monto_total}", color = Color(0xFF4A69A7))
                        }
                    }
                }
            }
        }
    }
}
