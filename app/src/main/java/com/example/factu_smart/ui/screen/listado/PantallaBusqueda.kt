package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.border
<<<<<<< HEAD
import androidx.compose.foundation.clickable
=======
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
<<<<<<< HEAD
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
=======
import androidx.compose.material.icons.filled.*
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
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
<<<<<<< HEAD

    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit,
    onCerrarSesion: () -> Unit,
    onFacturaClick: (Factura) -> Unit

) {

    var searchQuery by remember {

        mutableStateOf("")

    }

    var listaFacturas by remember {

        mutableStateOf<List<Factura>>(emptyList())

=======
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
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        try {

            listaFacturas =
<<<<<<< HEAD
                ClienteApi.servicio.obtenerFacturas()
=======
                ClienteApi.servicio
                    .obtenerFacturas()
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

<<<<<<< HEAD
    val facturasFiltradas = listaFacturas.filter {

            factura ->

        factura.nombre_receptor
            ?.contains(
                searchQuery,
                true
            ) == true ||

                factura.nit_receptor
                    ?.contains(
                        searchQuery,
                        true
                    ) == true ||

                factura.fecha_emision
                    ?.contains(
                        searchQuery,
                        true
                    ) == true ||

                factura.monto_total
                    .toString()
                    .contains(
                        searchQuery
                    ) ||

                factura.serie
                    ?.contains(
                        searchQuery,
                        true
                    ) == true

    }

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Spacer(
                    Modifier.height(
                        20.dp
                    )
                )

                Text(

                    "Factu-Smart",

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

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

                    onInicio,
                    onIngreso,
                    onBuscar

                )

            }

        ) {

                padding ->

            Column(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFFBCBCBC)
                        )
                        .padding(
                            padding
                        )
                        .padding(
                            16.dp
                        ),

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

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

                            null,

                            modifier =
                                Modifier.size(
                                    40.dp
                                )

                        )

                    }

                    Column(

                        horizontalAlignment =
                            Alignment.End

                    ) {

                        Icon(

                            Icons.Default.Description,

                            null

                        )

                        Text(

                            "Factu-Smart",

                            fontWeight =
                                FontWeight.Bold

                        )

                    }

                }

                Spacer(
                    Modifier.height(
                        20.dp
                    )
                )

                Text(

                    "BUSCAR FACTURAS",

                    fontSize = 24.sp,

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold

                )

                Spacer(
                    Modifier.height(
                        20.dp
                    )
                )

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                55.dp
                            )
                            .background(
                                Color.White
                            )
                            .border(
                                3.dp,
                                Color.Black
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    BasicTextField(

                        value =
                            searchQuery,

                        onValueChange = {

                            searchQuery = it

                        },

                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(
                                    horizontal =
                                        16.dp
                                ),

                        textStyle =
                            TextStyle(
                                fontSize = 18.sp
                            )

                    )

                    Icon(

                        Icons.Default.Search,

                        null,

                        modifier =
                            Modifier.padding(
                                10.dp
                            )

                    )

                }

                Spacer(
                    Modifier.height(
                        20.dp
                    )
                )

                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),

                    shape =
                        RoundedCornerShape(
                            20.dp
                        )

                ) {

                    LazyColumn {

                        itemsIndexed(

                            facturasFiltradas

                        ) {

                                index,
                                factura ->

                            Row(

                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            onFacturaClick(
                                                factura
                                            )

                                        }
                                        .padding(
                                            15.dp
                                        ),

                                verticalAlignment =
                                    Alignment.CenterVertically

                            ) {

                                Text(

                                    "${index + 1}"

                                )

                                Spacer(

                                    Modifier.width(
                                        10.dp
                                    )

                                )

                                Column(

                                    Modifier.weight(
                                        1f
                                    )

                                ) {

                                    Text(

                                        factura.nombre_receptor
                                            ?: "Cliente",

                                        fontWeight =
                                            FontWeight.Bold

                                    )

                                    Text(

                                        "NIT: ${
                                            factura.nit_receptor
                                                ?: "Sin NIT"
                                        }"

                                    )

                                    Text(

                                        "Fecha: ${
                                            factura.fecha_emision
                                                ?: "Sin fecha"
                                        }"

                                    )

                                }

                                Text(

                                    "Q${
                                        factura.monto_total
                                    }"

                                )

                            }

                            HorizontalDivider()

                        }

                    }

                }

            }

        }

    }

=======
    val facturasFiltradas =
        listaFacturas.filter { factura ->

            factura.nombre_receptor?.contains(
                searchQuery,
                ignoreCase = true
            ) == true ||

                    factura.nit_receptor?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true ||

                    factura.fecha_emision?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true ||

                    factura.monto_total
                        ?.toString()
                        ?.contains(searchQuery) == true ||

                    factura.serie?.contains(
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
                                            "Fecha: ${factura.fecha_emision ?: "Sin fecha"}",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )

                                        Text(
                                            "Serie: ${factura.serie ?: "Sin serie"}",
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
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
}