package com.example.inercia.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen() {
    val context = LocalContext.current
    val auth = Firebase.auth
    val user = auth.currentUser

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf(user?.email ?: "") }
    var reservaPendiente by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            val ref = Firebase.database.reference.child("usuarios").child(uid)
            ref.get().addOnSuccessListener {
                nombre = it.child("nombre").value?.toString() ?: ""
            }

            val reservaRef = Firebase.database.reference.child("reservas")
            reservaRef.get().addOnSuccessListener { snapshot ->
                snapshot.children.forEach { child ->
                    val data = child.value as? Map<*, *>
                    val rNombre = data?.get("nombre")?.toString()
                    val rFecha = data?.get("fecha")?.toString()
                    if (rNombre == nombre) {
                        reservaPendiente = "Tienes una reserva el $rFecha"
                        return@forEach
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Perfil",
            tint = Color.White,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu perfil", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        PerfilItem("Nombre", nombre)
        PerfilItem("Correo", correo)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            reservaPendiente ?: "No tienes reservas pendientes",
            color = if (reservaPendiente != null) Color(0xFFFF6F00) else Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                user?.email?.let { email ->
                    Firebase.auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Revisa tu correo para cambiar tu contraseña", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Cambiar contraseña", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PerfilItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
