package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
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

// NO SE USO NADA EXTERNO HERE
// Pantalla principal del listado de facturas
@Composable
fun PantallaListado(

    // Funciones de navegación entre pantallas
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    // Variable que almacena la lista de facturas obtenidas
    var lista by remember {
        mutableStateOf<List<Factura>>(emptyList())
    }

    // Controla el estado del menú lateral
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    // Permite ejecutar corrutinas dentro de Compose
    val scope = rememberCoroutineScope()

    // Se ejecuta una vez al abrir la pantalla
    LaunchedEffect(Unit) {

        try {

            // Obtiene las facturas desde la API
            lista =
                ClienteApi.servicio
                    .obtenerFacturas()

        } catch (e: Exception) {

            // Muestra errores en consola
            e.printStackTrace()
        }
    }

    // Contenedor del menú lateral
    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // Nombre mostrado en el menú
                Text(
                    text = "Factu-Smart",

                    modifier =
                        Modifier.padding(16.dp),

                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                // Opción para cerrar sesión
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
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) {

        // Diseño principal de pantalla
        Scaffold(

            // Barra inferior de navegación
            bottomBar = {

                BottomNavigationBar(
                    onInicio = onInicio,
                    onIngreso = onIngreso,
                    onBuscar = onBuscar
                )
            }

        ) { padding ->

            // Contenedor principal
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD9D9D9))
                    .padding(padding)
                    .padding(16.dp)

            ) {

                // Fila superior con botón menú y título
                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    // Botón para abrir menú lateral
                    IconButton(

                        onClick = {

                            scope.launch {

                                drawerState.open()
                            }
                        }

                    ) {

                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    // Título principal
                    Text(

                        "Facturas Guardadas",

                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                // Lista desplazable de facturas
                LazyColumn(

                    modifier =
                        Modifier.fillMaxSize()

                ) {

                    // Recorre cada factura obtenida
                    items(lista) { factura ->

                        // Tarjeta individual por factura
                        Card(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),

                            colors =
                                CardDefaults.cardColors(

                                    containerColor =
                                        Color.White
                                )
                        ) {

                            Column(

                                modifier =
                                    Modifier.padding(16.dp)

                            ) {

                                // Nombre del emisor
                                Text(

                                    "Cliente: ${factura.nombre_emisor}",

                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyLarge
                                )

                                // NIT del emisor
                                Text(

                                    "NIT: ${factura.nit_emisor}",

                                    color =
                                        Color.Gray
                                )

                                // Monto total de factura
                                Text(

                                    "Total: Q${factura.monto_total}",

                                    color =
                                        Color(0xFF4A69A7)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}