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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    onCerrarSesion: () -> Unit
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var listaFacturas by remember {
        mutableStateOf<List<Factura>>(emptyList())
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        try {

            listaFacturas =
                ClienteApi.servicio
                    .obtenerFacturas()

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

    val facturasFiltradas =
        listaFacturas.filter { factura ->

            factura.descripcion?.contains(
                searchQuery,
                ignoreCase = true
            ) == true ||

                    factura.nombre_receptor?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true ||

                    factura.nit_receptor?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true ||

                    factura.monto_total
                        ?.toString()
                        ?.contains(searchQuery) == true ||

                    factura.fecha_emision?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true

        }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )
                Text(
                    text = "Factu-Smart",
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
                    "BUSCAR FACTURAS",
                    fontSize = 24.sp,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color.White)
                        .border(3.dp, Color.Black),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight =
                                FontWeight.Bold
                        ),

                        singleLine = true,
                        decorationBox = {
                                innerTextField ->
                            if (
                                searchQuery.isEmpty()
                            ) {
                                Text(
                                    "BUSCAR...",
                                    color =
                                        Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(50.dp)
                            .background(Color.White)
                            .border(3.dp, Color.Black),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.Search,
                            null
                        )
                    }
                }
                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape =
                        RoundedCornerShape(25.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {
                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {
                        Text(
                            "---------------------------------",
                            modifier =
                                Modifier.fillMaxWidth(),
                            textAlign =
                                TextAlign.Center,
                            color =
                                Color.Gray
                        )
                        LazyColumn {
                            itemsIndexed(
                                facturasFiltradas
                            ) {
                                    index,
                                    factura ->

                                Row(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${index + 1}"
                                    )
                                    Spacer(
                                        modifier =
                                            Modifier.width(10.dp)
                                    )
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            factura.nombre_receptor ?: "CLIENTE",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Spacer(
                                            modifier = Modifier.height(4.dp)
                                        )
                                        Text(
                                            "NIT: ${factura.nit_receptor ?: "Sin NIT"}",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )

                                        Text(
                                            "Factura: ${factura.descripcion ?: "Sin descripción"}",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )

                                        Text(
                                            "Autorización: ${factura.numero_autorizacion ?: "No disponible"}",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Text(
                                        "Q${factura.monto_total}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}