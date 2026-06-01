package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi
import kotlinx.coroutines.launch

@Composable
fun PantallaListado(
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit,
    onFacturaClick: (Factura) -> Unit
) {

    var lista by remember {
        mutableStateOf<List<Factura>>(emptyList())
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            lista = ClienteApi.servicio.obtenerFacturas()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
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
                    .background(Color(0xFFD9D9D9))
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "Facturas Guardadas",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(Modifier.height(10.dp))

                LazyColumn {

                    items(lista) { factura ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onFacturaClick(factura)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    "Cliente: ${factura.nombre_emisor}",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    "NIT: ${factura.nit_emisor}",
                                    color = Color.Gray
                                )

                                Text(
                                    "Total: Q${factura.monto_total}",
                                    color = Color(0xFF4A69A7)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}