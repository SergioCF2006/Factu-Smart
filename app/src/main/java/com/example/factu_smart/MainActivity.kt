package com.example.factu_smart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.factu_smart.data.model.Factura

import com.example.factu_smart.data.model.Usuario
import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.ui.screen.listado.*

import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException

enum class Screen {
    LOGIN,
    MENU_PRINCIPAL,
    SUBIR_FACTURAS,
    LISTADO,
    BUSQUEDA,
    DETALLE_FACTURA,
    REGISTER
}

class MainActivity : ComponentActivity() {

    lateinit var googleClient: GoogleSignInClient
    lateinit var launcherGoogle: ActivityResultLauncher<Intent>

    var currentScreen by mutableStateOf(Screen.LOGIN)

    var facturaSeleccionada by mutableStateOf<Factura?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val opciones = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .requestIdToken(
                "125866495628-q48hj370fifuchns68qg2ul13avn192m.apps.googleusercontent.com"
            )
            .build()

        googleClient = GoogleSignIn.getClient(this, opciones)

        launcherGoogle = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val cuenta = task.getResult(ApiException::class.java)

                    registrarUsuarioEnBD(
                        cuenta.email ?: "",
                        cuenta.displayName ?: "Usuario Google"
                    )

                } catch (e: ApiException) {

                    Toast.makeText(
                        this,
                        "Error Google: ${e.statusCode}",
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("LOGIN", "Error Google: ${e.statusCode}")
                }
            }
        }

        setContent {

            when (currentScreen) {

                Screen.LOGIN -> LoginScreen(
                    onGoogleClick = { loginGoogle() },
                    onLoginSuccess = { correo, password ->
                        loginUsuario(correo, password)
                    },
                    onRegisterClick = {
                        currentScreen = Screen.REGISTER
                    }
                )

                Screen.REGISTER -> RegisterScreen(
                    onRegister = { nombre, correo, password ->
                        registrarNuevoUsuario(nombre, correo, password)
                    },
                    onBack = {
                        currentScreen = Screen.LOGIN
                    }
                )

                Screen.MENU_PRINCIPAL -> MenuPrincipalScreen(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA },
                    onCerrarSesion = { currentScreen = Screen.LOGIN }
                )

                Screen.SUBIR_FACTURAS -> SubirFacturasScreen(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onBuscar = { currentScreen = Screen.BUSQUEDA },
                    onCerrarSesion = { currentScreen = Screen.LOGIN }
                )

                Screen.LISTADO -> PantallaListado(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA },
                    onCerrarSesion = { currentScreen = Screen.LOGIN },
                    onFacturaClick = { factura ->

                        println("Factura: $factura")
                    }
                )

                Screen.BUSQUEDA -> PantallaBusqueda(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA },
                    onCerrarSesion = { currentScreen = Screen.LOGIN },
                    onFacturaClick = { factura ->

                        facturaSeleccionada = factura
                        currentScreen = Screen.DETALLE_FACTURA

                    }
                )
                Screen.DETALLE_FACTURA -> {

                    facturaSeleccionada?.let { factura ->

                        PantallaDetalleFactura(
                            factura = factura,
                            onVolver = {
                                currentScreen = Screen.BUSQUEDA
                            }
                        )

                    }

                }
            }
        }

    }

    private fun loginUsuario(correo: String, password: String) {

        lifecycleScope.launch {

            try {

                val usuarios =
                    ClienteApi.servicio.obtenerUsuarioPorCorreo("eq.$correo")

                if (usuarios.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Usuario no existe", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val usuario = usuarios.first()

                if (usuario.contra == password) {

                    Toast.makeText(this@MainActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                    currentScreen = Screen.MENU_PRINCIPAL

                } else {

                    Toast.makeText(this@MainActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN", "Error: ${e.message}")
            }
        }
    }

    private fun registrarNuevoUsuario(nombre: String, correo: String, password: String) {

        lifecycleScope.launch {

            try {

                val usuarios =
                    ClienteApi.servicio.obtenerUsuarioPorCorreo("eq.$correo")

                if (usuarios.isNotEmpty()) {
                    Toast.makeText(this@MainActivity, "Usuario ya existe", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val response =
                    ClienteApi.servicio.guardarUsuario(
                        Usuario(nombre = nombre, correo = correo, contra = password)
                    )

                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    currentScreen = Screen.LOGIN
                }

            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                Log.e("REGISTER", "Error: ${e.message}")
            }
        }
    }

    private fun registrarUsuarioEnBD(correo: String, nombre: String) {

        lifecycleScope.launch {

            try {

                val usuarios =
                    ClienteApi.servicio.obtenerUsuarioPorCorreo("eq.$correo")

                if (usuarios.isNotEmpty()) {
                    currentScreen = Screen.MENU_PRINCIPAL
                    return@launch
                }

                ClienteApi.servicio.guardarUsuario(
                    Usuario(
                        correo = correo,
                        nombre = nombre,
                        contra = null
                    )
                )

                currentScreen = Screen.MENU_PRINCIPAL

            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loginGoogle() {

        Log.d("GOOGLE", "Boton Google presionado")

        googleClient.signOut().addOnCompleteListener {
            launcherGoogle.launch(googleClient.signInIntent)
        }
    }
}