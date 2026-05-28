package com.example.factu_smart.ui.screen.listado

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.R

@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onBack: () -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                "Crear cuenta",
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // NOMBRE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(25.dp))
                    .padding(14.dp)
            ) {
                BasicTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        if (nombre.isEmpty()) {
                            Text("Nombre", color = Color.Gray)
                        }
                        it()
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // CORREO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(25.dp))
                    .padding(14.dp)
            ) {
                BasicTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        if (correo.isEmpty()) {
                            Text("Correo", color = Color.Gray)
                        }
                        it()
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PASSWORD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(25.dp))
                    .padding(14.dp)
            ) {
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        if (password.isEmpty()) {
                            Text("Contraseña", color = Color.Gray)
                        }
                        it()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    if (
                        nombre.isNotBlank() &&
                        correo.isNotBlank() &&
                        password.isNotBlank()
                    ) {

                        onRegister(nombre, correo, password)

                    } else {

                        Toast.makeText(
                            context,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { onBack() }) {
                Text(
                    "Volver al login",
                    color = Color.White
                )
            }
        }
    }
}