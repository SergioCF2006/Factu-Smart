package com.example.factu_smart.ui.screen.listado

import android.widget.Toast
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.factu_smart.R

// NO SE USO NADA EXTERNO HERE
// Pantalla principal de inicio de sesión
@Composable
fun LoginScreen(

    // Acción para iniciar sesión con Google
    onGoogleClick: () -> Unit,

    // Acción que ocurre cuando el login es correcto
    onLoginSuccess: (String, String) -> Unit,

    // Acción para abrir pantalla de registro
    onRegisterClick: () -> Unit

) {

    // Variable que guarda el correo ingresado
    var correo by remember {
        mutableStateOf("")
    }

    // Variable que guarda la contraseña ingresada
    var password by remember {
        mutableStateOf("")
    }

    // Obtiene el contexto actual para usar Toast
    val context = LocalContext.current

    // Contenedor principal de toda la pantalla
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Imagen de fondo del login
        Image(

            painter =
                painterResource(
                    id = R.drawable.fondo_login
                ),

            contentDescription = null,

            modifier =
                Modifier.fillMaxSize(),

            contentScale =
                ContentScale.Crop

        )

        // Contenedor de elementos alineados al fondo
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .padding(bottom = 80.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Bottom

        ){

            // Caja visual del campo correo
            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.85f),
                        RoundedCornerShape(25.dp)
                    )
                    .padding(14.dp)

            ) {

                // Campo de texto para correo
                BasicTextField(

                    value = correo,

                    onValueChange = {
                        correo = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    decorationBox = {

                        // Placeholder cuando está vacío
                        if (correo.isEmpty()) {

                            Text(
                                "Correo",
                                color = Color.Gray
                            )
                        }

                        it()
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // Caja visual del campo contraseña
            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.85f),
                        RoundedCornerShape(25.dp)
                    )
                    .padding(14.dp)

            ) {

                // Campo para contraseña
                BasicTextField(

                    value = password,

                    onValueChange = {
                        password = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    decorationBox = {

                        // Placeholder cuando está vacío
                        if (password.isEmpty()) {

                            Text(
                                "Contraseña",
                                color = Color.Gray
                            )
                        }

                        it()
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // Botón principal de inicio de sesión
            Button(

                onClick = {

                    // Valida que los campos tengan contenido
                    if (
                        correo.isNotBlank()
                        &&
                        password.isNotBlank()
                    ) {

                        // Ejecuta login exitoso
                        onLoginSuccess(
                            correo,
                            password
                        )

                    } else {

                        // Mensaje si faltan datos
                        Toast.makeText(

                            context,

                            "Por favor, rellena todos los campos",

                            Toast.LENGTH_SHORT

                        ).show()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                shape =
                    RoundedCornerShape(30.dp)

            ) {

                Text(
                    "Iniciar sesión"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // Botón de autenticación con Google
            Button(

                onClick = {

                    onGoogleClick()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color.White
                    ),

                shape =
                    RoundedCornerShape(30.dp)

            ) {

                Row(

                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.Center

                ) {

                    // Ícono de Google
                    Image(

                        painter =
                            painterResource(
                                id = R.drawable.ic_google
                            ),

                        contentDescription =
                            "Google",

                        modifier =
                            Modifier.size(20.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Text(

                        "Continuar con Google",

                        color =
                            Color.Black

                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // Contenedor opciones adicionales
            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                // Texto recuperación contraseña
                Text(

                    "¿Olvidaste tu contraseña?",

                    color =
                        Color.White,

                    fontSize =
                        12.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                // Botón de registro
                TextButton(

                    onClick = {

                        onRegisterClick()
                    }

                ) {

                    Text(

                        "Registrarse",

                        color =
                            Color.White

                    )
                }
            }
        }
    }
}

// Vista previa para Android Studio
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    LoginScreen(

        onGoogleClick = {},

        onLoginSuccess = { _, _ -> },

        onRegisterClick = {}

    )
}