package com.example.factu_smart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import com.example.factu_smart.data.remote.ClienteApi
import com.example.factu_smart.ui.screen.listado.LoginScreen
import com.example.factu_smart.ui.screen.listado.MenuPrincipalScreen
import com.example.factu_smart.ui.screen.listado.SubirFacturasScreen
import com.example.factu_smart.ui.screen.listado.PantallaListado
import com.example.factu_smart.ui.screen.listado.PantallaBusqueda

import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException

enum class Screen {
    LOGIN,
    MENU_PRINCIPAL,
    SUBIR_FACTURAS,
    LISTADO,
    BUSQUEDA
}

class MainActivity : ComponentActivity() {

    lateinit var googleClient: GoogleSignInClient
    lateinit var launcherGoogle: ActivityResultLauncher<Intent>

    var currentScreen by mutableStateOf(Screen.LOGIN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val opciones = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("125866495628-q48hj370fifuchns68qg2ul13avn192m.apps.googleusercontent.com")
            .build()

        googleClient = GoogleSignIn.getClient(this, opciones)

        launcherGoogle = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val cuenta = task.getResult(ApiException::class.java)
                    registrarUsuarioEnBD(cuenta.email ?: "", cuenta.displayName ?: "Usuario Google")
                } catch (e: ApiException) {
                    Log.e("LOGIN_DEBUG", "❌ ERROR GOOGLE. Código: ${e.statusCode}")
                    Toast.makeText(this, "Error Google: ${e.statusCode}", Toast.LENGTH_LONG).show()
                }
            }
        }

        setContent {
            when (currentScreen) {
                Screen.LOGIN -> LoginScreen(
                    onGoogleClick = { loginGoogle() },
                    onLoginSuccess = { correo, _ -> registrarUsuarioEnBD(correo, correo.split("@")[0]) }
                )
                Screen.MENU_PRINCIPAL -> MenuPrincipalScreen(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA }
                )
                Screen.SUBIR_FACTURAS -> SubirFacturasScreen(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onBuscar = { currentScreen = Screen.BUSQUEDA }
                )
                Screen.LISTADO -> PantallaListado(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA }
                )
                Screen.BUSQUEDA -> PantallaBusqueda(
                    onInicio = { currentScreen = Screen.MENU_PRINCIPAL },
                    onIngreso = { currentScreen = Screen.SUBIR_FACTURAS },
                    onBuscar = { currentScreen = Screen.BUSQUEDA }
                )
            }
        }
    }

    private fun registrarUsuarioEnBD(correo: String, nombre: String) {
        lifecycleScope.launch {
            try {
                val response = ClienteApi.servicio.guardarUsuario(
                    com.example.factu_smart.data.model.Usuario(correo = correo, nombre = nombre)
                )
                
                if (response.isSuccessful || response.code() == 409) {
                    currentScreen = Screen.MENU_PRINCIPAL
                } else {
                    Log.e("SUPABASE", "❌ Error servidor: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Error BD: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("SUPABASE", "❌ Error conexión: ${e.message}")
                Toast.makeText(this@MainActivity, "Sin conexión a la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loginGoogle() {
        googleClient.signOut().addOnCompleteListener {
            launcherGoogle.launch(googleClient.signInIntent)
        }
    }
}
