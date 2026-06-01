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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.R

@Composable
fun LoginScreen(
    onGoogleClick: () -> Unit,
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {

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

            // EMAIL
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
                    singleLine = true,
                    decorationBox = { inner ->
                        if (correo.isEmpty()) {
                            Text("Correo", color = Color.Gray)
                        }
                        inner()
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PASSWORD (CORREGIDO)
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
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    decorationBox = { inner ->
                        if (password.isEmpty()) {
                            Text("Contraseña", color = Color.Gray)
                        }
                        inner()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LOGIN BUTTON
            Button(
                onClick = {

                    val email = correo.trim()
                    val pass = password.trim()

                    if (email.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    onLoginSuccess(email, pass)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // GOOGLE
            Button(
                onClick = { onGoogleClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(30.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Continuar con Google", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { onRegisterClick() }) {
                Text("Registrarse", color = Color.White)
            }
        }
    }
}