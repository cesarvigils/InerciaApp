package com.example.inercia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inercia.ui.screens.*
import com.example.inercia.ui.theme.InerciaTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            InerciaTheme {
                val navController = rememberNavController()
                val user = FirebaseAuth.getInstance().currentUser
                val startDestination = if (user != null) "home" else "login"

                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("home") { HomeScreen(navController) }
                        composable("reserva") { ReservarScreen(navController) }
                        composable("perfil") { PerfilScreen() }
                        composable(
                            "thankyou/{nombre}/{fecha}/{rigTipo}/{rigs}/{total}/{metodo}/{codigo}"
                        ) { backStackEntry ->
                            ThankYouScreen(
                                nombre = backStackEntry.arguments?.getString("nombre") ?: "",
                                fecha = backStackEntry.arguments?.getString("fecha") ?: "",
                                rigTipo = backStackEntry.arguments?.getString("rigTipo") ?: "",
                                rigs = backStackEntry.arguments?.getString("rigs") ?: "",
                                total = backStackEntry.arguments?.getString("total") ?: "",
                                metodo = backStackEntry.arguments?.getString("metodo") ?: "",
                                codigo = backStackEntry.arguments?.getString("codigo") ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        TopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp)
                )
            },
            actions = {
                IconButton(onClick = { navController.navigate("perfil") }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Cuenta", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        ContentBody(navController)
    }
}

@Composable
fun ContentBody(navController: NavController) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.sim1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(bottom = 20.dp),
                contentScale = ContentScale.Crop
            )

            Button(
                onClick = { navController.navigate("reserva") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "RESERVA AHORA EN LÍNEA",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "SOBRE NOSOTROS",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Somos el primer centro de simulación de carreras (Sim Racing) en línea real. Vive la experiencia profesional en simuladores avanzados.",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.sim2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }
    }
}
