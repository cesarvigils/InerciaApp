package com.example.inercia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.inercia.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF6F00),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF6F00),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    mensaje = "Debes llenar todos los campos"
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            if (auth.currentUser?.isEmailVerified == true) {
                                mensaje = "✅ Bienvenido"
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            } else {
                                mensaje = "Debes verificar tu correo"
                            }
                        }
                        .addOnFailureListener {
                            mensaje = "Error al iniciar sesión"
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Iniciar sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    mensaje = "Debes llenar todos los campos"
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            auth.currentUser?.sendEmailVerification()
                            mensaje = "Cuenta creada, revisa tu correo"
                        }
                        .addOnFailureListener {
                            mensaje = "Error al registrar, verifica tus datos"
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Crear cuenta", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(mensaje, color = Color.White)
    }
}
