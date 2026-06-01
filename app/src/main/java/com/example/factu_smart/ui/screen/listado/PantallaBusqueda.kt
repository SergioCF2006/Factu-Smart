package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.launch

@Composable
fun PantallaBusqueda(
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit,
    onFacturaClick: (Factura) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }
    var listaFacturas by remember { mutableStateOf<List<Factura>>(emptyList()) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            listaFacturas = ClienteApi.servicio.obtenerFacturas()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val facturasFiltradas = listaFacturas.filter { factura ->
        factura.nombre_receptor?.contains(searchQuery, true) == true ||
                factura.nit_receptor?.contains(searchQuery, true) == true ||
                factura.fecha_emision?.contains(searchQuery, true) == true ||
                (factura.monto_total?.toString()?.contains(searchQuery) == true) ||
                factura.serie?.contains(searchQuery, true) == true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Spacer(Modifier.height(20.dp))

                Text(
                    "Factu-Smart",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = { onCerrarSesion() },
                    icon = {
                        Icon(Icons.Default.ExitToApp, null)
                    }
                )
            }
        }
    ) {

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
                    .background(Color(0xFFBCBCBC))
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Icon(Icons.Default.Menu, null)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Icon(Icons.Default.Description, null)
                        Text("Factu-Smart", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "BUSCAR FACTURAS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(20.dp))

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
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(fontSize = 18.sp),
                        singleLine = true
                    )

                    Icon(
                        Icons.Default.Search,
                        null,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    LazyColumn {

                        itemsIndexed(facturasFiltradas) { index, factura ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onFacturaClick(factura) }
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text("${index + 1}")

                                Spacer(Modifier.width(10.dp))

                                Column(Modifier.weight(1f)) {

                                    Text(
                                        factura.nombre_receptor ?: "CLIENTE",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text("NIT: ${factura.nit_receptor ?: "Sin NIT"}")

                                    Text("Fecha: ${factura.fecha_emision ?: "Sin fecha"}")

                                    Text("Serie: ${factura.serie ?: "Sin serie"}")
                                }

                                Text("Q${factura.monto_total ?: 0.0}")
                            }

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}