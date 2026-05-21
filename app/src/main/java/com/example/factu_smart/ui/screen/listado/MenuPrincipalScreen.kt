package com.example.factu_smart.ui.screen.listado

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.remote.ClienteApi
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MenuPrincipalScreen(
    onInicio: () -> Unit = {},
    onIngreso: () -> Unit = {},
    onBuscar: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val drawerState =
        rememberDrawerState(
            DrawerValue.Closed
        )

    val scope =
        rememberCoroutineScope()

    var listaFacturas by remember {

        mutableStateOf<List<Factura>>(
            emptyList()
        )
    }

    LaunchedEffect(Unit){

        try{
            listaFacturas =
                ClienteApi
                    .servicio
                    .obtenerFacturas()
        }catch(e:Exception){
            e.printStackTrace()
        }
    }

    val mesActual =
        LocalDate.now().month

    val nombreMes = when(mesActual){

        java.time.Month.JANUARY -> "ENERO"
        java.time.Month.FEBRUARY -> "FEBRERO"
        java.time.Month.MARCH -> "MARZO"
        java.time.Month.APRIL -> "ABRIL"
        java.time.Month.MAY -> "MAYO"
        java.time.Month.JUNE -> "JUNIO"
        java.time.Month.JULY -> "JULIO"
        java.time.Month.AUGUST -> "AGOSTO"
        java.time.Month.SEPTEMBER -> "SEPTIEMBRE"
        java.time.Month.OCTOBER -> "OCTUBRE"
        java.time.Month.NOVEMBER -> "NOVIEMBRE"
        java.time.Month.DECEMBER -> "DICIEMBRE"

    }

    val totalMes =

        listaFacturas
            .filter {
                try{
                    LocalDate
                        .parse(it.fecha_emision)
                        .month == mesActual
                }catch(e:Exception){
                    false
                }
            }

            .sumOf {
                it.monto_total ?: 0.0
            }

    val clienteFrecuente =
        listaFacturas
            .groupBy {
                it.nit_receptor
                    ?: "SIN NIT"
            }
            .maxByOrNull {
                it.value.size
            }

    val nombreCliente =
        clienteFrecuente
            ?.value
            ?.firstOrNull()
            ?.nombre_receptor
            ?: "SIN DATOS"

    val totalCliente =
        clienteFrecuente
            ?.value
            ?.sumOf {
                it.monto_total ?: 0.0
            }
            ?: 0.0

    val totalFacturasCliente =
        clienteFrecuente
            ?.value
            ?.size
            ?: 0

    val actividadMensual =
        (1..12).map { mes ->
            listaFacturas
                .filter {
                    try{
                        LocalDate
                            .parse(
                                it.fecha_emision
                            )
                            .monthValue == mes
                    }catch(e:Exception){
                        false
                    }
                }
                .sumOf {
                    it.monto_total ?: 0.0
                }
        }

    val maximoGrafica =
        actividadMensual
            .maxOrNull()
            ?: 1.0
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(
                    Modifier.height(20.dp)
                )
                Text(
                    "Factu-Smart",
                    Modifier.padding(16.dp),
                    fontWeight =
                        FontWeight.Bold,
                    fontSize = 22.sp
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
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFFD9D9D9)
                    )
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
                        Alignment.CenterVertically
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
                                Modifier.size(35.dp)
                        )
                    }
                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Factu-Smart",
                            fontSize = 24.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                        Text(
                            "GESTIÓN DE FACTURAS ELECTRÓNICAS",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(
                        Modifier.size(35.dp)
                    )
                }
                Spacer(
                    Modifier.height(20.dp)
                )
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    DashboardCard(
                        modifier =
                            Modifier.weight(1f),
                        title =
                            "TOTAL FACTURADO\nMES / $nombreMes",
                        value =
                            "Q %.2f"
                                .format(totalMes),
                        icon =
                            Icons.Default.AttachMoney
                    )

                    DashboardCard(
                        modifier =
                            Modifier.weight(1f),
                        title =
                            "CLIENTE FRECUENTE",
                        subtitle =
                            nombreCliente,
                        value =
                            "Q %.2f"
                                .format(totalCliente),
                        extra =
                            "FACTURAS: $totalFacturasCliente"
                    )
                }

                Spacer(
                    Modifier.height(20.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape =
                        RoundedCornerShape(25.dp)
                ){

                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ){

                        Text(
                            "Actividad Mensual",
                            fontWeight =
                                FontWeight.Bold
                        )
                        Spacer(
                            Modifier.height(20.dp)
                        )
                        val meses = listOf(
                            "ENE","FEB","MAR","ABR",
                            "MAY","JUN","JUL","AGO",
                            "SEP","OCT","NOV","DIC"
                        )

                        Row(
                            modifier =
                                Modifier.fillMaxSize(),
                            horizontalArrangement =
                                Arrangement.SpaceEvenly,
                            verticalAlignment =
                                Alignment.Bottom
                        ){

                            actividadMensual
                                .forEachIndexed {
                                        index,
                                        totalMes ->
                                    Column(
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally,
                                        verticalArrangement =
                                            Arrangement.Bottom
                                    ){
                                        val altura =
                                            if(maximoGrafica > 0)
                                                ((totalMes /
                                                        maximoGrafica)
                                                        *120).dp
                                            else
                                                10.dp
                                        Box(
                                            modifier =
                                                Modifier
                                                    .width(18.dp)
                                                    .height(
                                                        altura
                                                    )
                                                    .background(
                                                        Color(
                                                            0xFF4A69A7
                                                        ),
                                                        RoundedCornerShape(
                                                            topStart = 4.dp,
                                                            topEnd = 4.dp
                                                        )
                                                    )
                                        )
                                        Spacer(
                                            Modifier.height(5.dp)
                                        )
                                        Text(
                                            meses[index],
                                            fontSize = 9.sp,
                                            color = Color.Gray
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

@Composable
fun DashboardCard(
    modifier: Modifier,
    title:String,
    subtitle:String?=null,
    value:String,
    extra:String?=null,
    icon:ImageVector?=null
){
    Card(
        modifier=
            modifier.height(160.dp),
        shape=
            RoundedCornerShape(25.dp)
    ){

        Column(
            modifier=
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            horizontalAlignment=
                Alignment.CenterHorizontally,
            verticalArrangement=
                Arrangement.Center
        ){
            icon?.let{
                Icon(
                    it,
                    null
                )
            }
            Text(
                title,
                fontWeight=
                    FontWeight.Bold,
                textAlign=
                    TextAlign.Center,
                fontSize=12.sp
            )

            subtitle?.let{
                Text(
                    it,
                    color=
                        Color.Gray,
                    fontSize=10.sp
                )
            }
            Spacer(
                Modifier.height(8.dp)
            )
            Text(
                value,
                color=
                    Color(0xFF4A69A7),
                fontWeight=
                    FontWeight.ExtraBold
            )

            extra?.let{
                Text(
                    it,
                    color=
                        Color.Gray,
                    fontSize=9.sp
                )
            }
        }
    }
}
@Composable
fun BottomNavigationBar(
    onInicio: () -> Unit,
    onIngreso: () -> Unit,
    onBuscar: () -> Unit
){
    Surface(
        color = Color(0xFF4A69A7),
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),

        modifier = Modifier

            .fillMaxWidth()
            .height(80.dp)
    ){
        Row(
            modifier =
                Modifier.fillMaxSize(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ){

            NavItem(
                icon =
                    Icons.Default.Home,
                label =
                    "INICIO",
                onClick =
                    onInicio
            )

            NavItem(
                icon =
                    Icons.Default.AttachMoney,
                label =
                    "INGRESO",
                onClick =
                    onIngreso
            )

            NavItem(
                icon =
                    Icons.Default.Search,
                label =
                    "BUSCAR",
                onClick =
                    onBuscar
            )
        }
    }
}

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
){
    IconButton(
        onClick = onClick
    ){
        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ){
            Icon(
                imageVector =
                    icon,
                contentDescription =
                    label,
                tint =
                    Color.White,
                modifier =
                    Modifier.size(28.dp)
            )
            Text(
                text =
                    label,
                color =
                    Color.White,
                fontSize =
                    10.sp
            )
        }
    }
}