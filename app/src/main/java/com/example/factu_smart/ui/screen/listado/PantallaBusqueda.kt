package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi

@Composable
fun PantallaBusqueda(
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var listaFacturas by remember { mutableStateOf<List<Factura>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            listaFacturas = ClienteApi.servicio.obtenerFacturas()
        } catch (e: Exception) { e.printStackTrace() }
    }

    val facturasFiltradas = listaFacturas.filter {
        it.descripcion?.contains(searchQuery, ignoreCase = true) == true ||
        it.nombre_emisor?.contains(searchQuery, ignoreCase = true) == true
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(onInicio = onInicio, onIngreso = onIngreso, onBuscar = onBuscar)
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
            // Header
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
            Text("BUSCAR FACTURAS", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(modifier = Modifier.height(20.dp))

            // Barra de Búsqueda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Color.White)
                    .border(3.dp, Color.Black),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) Text("BUSCAR...", color = Color.LightGray, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        innerTextField()
                    }
                )
                Box(
                    modifier = Modifier.fillMaxHeight().width(50.dp).background(Color.White).border(3.dp, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Search, null, modifier = Modifier.size(35.dp), tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Lista de Facturas
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("---------------------------------", textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                    
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(facturasFiltradas) { index, factura ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${index + 1}", fontSize = 22.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(30.dp))
                                Box(modifier = Modifier.width(2.dp).height(40.dp).background(Color.LightGray))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(factura.descripcion?.uppercase() ?: "FACTURA", fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text(factura.nombre_emisor?.uppercase() ?: "DESCONOCIDO", fontSize = 10.sp, color = Color.Gray)
                                }
                                Box(modifier = Modifier.width(2.dp).height(40.dp).background(Color.LightGray))
                                Text("Q${factura.monto_total}", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
