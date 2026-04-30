package com.example.factu_smart.ui.screen.listado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuPrincipalScreen(
    onInicio: () -> Unit = {},
    onIngreso: () -> Unit = {},
    onBuscar: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(onInicio, onIngreso, onBuscar)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9))
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // CABECERA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(35.dp))
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Factu-Smart", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("GESTIÓN DE FACTURAS ELECTRÓNICAS", fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.size(35.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TARJETAS SUPERIORES
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "TOTAL FACTURADO\nMES / MARZO",
                    value = "Q 5,212.15",
                    icon = Icons.Default.AttachMoney
                )
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "CLIENTES FRECUENTES",
                    subtitle = "JOSE HERNANDEZ ORTIZ",
                    value = "Q 1,012.15",
                    extra = "TOTAL FACTURAS: 89"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ÁREA DE GRÁFICA (Simulada)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Actividad Mensual", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        listOf(40, 70, 90, 110, 80, 60, 40).forEach { height ->
                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(height.dp)
                                    .background(Color(0xFF4A69A7), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
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
    title: String,
    subtitle: String? = null,
    value: String,
    extra: String? = null,
    icon: ImageVector? = null
) {
    Card(
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icon != null) Icon(icon, null, modifier = Modifier.size(24.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            if (subtitle != null) Text(subtitle, fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4A69A7))
            if (extra != null) Text(extra, fontSize = 9.sp, color = Color.Gray)
        }
    }
}

@Composable
fun BottomNavigationBar(onInicio: () -> Unit, onIngreso: () -> Unit, onBuscar: () -> Unit) {
    Surface(
        color = Color(0xFF4A69A7),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Default.Home, "INICIO", onInicio)
            NavItem(Icons.Default.AttachMoney, "INGRESO", onIngreso)
            NavItem(Icons.Default.Search, "BUSCAR", onBuscar)
        }
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(28.dp))
            Text(label, color = Color.White, fontSize = 10.sp)
        }
    }
}
