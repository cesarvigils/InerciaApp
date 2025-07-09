package com.example.inercia.ui.screens

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inercia.R
import com.example.inercia.models.Reserva
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.random.Random
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservarScreen(navController: NavController) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    var nombre by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            val ref = Firebase.database.reference.child("usuarios").child(uid)
            ref.get().addOnSuccessListener {
                nombre = it.child("nombre").value?.toString() ?: ""
            }
        }
    }

    var fecha by remember { mutableStateOf("") }
    var rigTipo by remember { mutableStateOf("Normal") }
    var horas by remember { mutableStateOf("1") }
    var rigs by remember { mutableStateOf("1") }
    var metodoPago by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarMetodoPago by remember { mutableStateOf(false) }

    val precioHora = if (rigTipo == "Premium") 300 else 200
    val total = horas.toIntOrNull()?.times(rigs.toIntOrNull() ?: 0)?.times(precioHora) ?: 0
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        TopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Cuenta", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Reserva tu Simulador", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            Text("Tu nombre: $nombre", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            FechaPickerInercia(fecha) { selectedFecha -> fecha = selectedFecha }

            Spacer(modifier = Modifier.height(16.dp))

            SelectDropdown("Tipo de rig", rigTipo, listOf("Normal", "Premium")) { selected -> rigTipo = selected }

            Spacer(modifier = Modifier.height(12.dp))

            SelectDropdown("Horas", horas, (1..5).map { it.toString() }) { selected -> horas = selected }

            Spacer(modifier = Modifier.height(12.dp))

            SelectDropdown("Rigs", rigs, (1..9).map { it.toString() }) { selected -> rigs = selected }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Total: L. $total", color = Color(0xFFFF6F00), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { mostrarMetodoPago = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (metodoPago.isEmpty()) "Seleccionar método de pago" else metodoPago, color = Color.White)
            }

            if (mostrarMetodoPago) {
                MetodoPagoScreen { metodo ->
                    metodoPago = metodo
                    mostrarMetodoPago = false
                    if (metodo == "Transferencia bancaria") {
                        mensaje = "Revisa los datos bancarios en la siguiente pantalla"
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (metodoPago == "Transferencia bancaria") {
                Button(
                    onClick = {
                        val url = "https://wa.me/50499999999?text=Hola%20adjunto%20comprobante%20de%20pago"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enviar comprobante", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val rigsInt = rigs.toIntOrNull()
                    val horasInt = horas.toIntOrNull()

                    if (nombre.isNotEmpty() && fecha.isNotEmpty() && metodoPago.isNotEmpty() && rigsInt != null && horasInt != null) {
                        if (rigTipo == "Premium" && rigsInt > 1) {
                            mensaje = "Solo puedes reservar 1 rig Premium"
                        } else {
                            val precioHora = if (rigTipo == "Premium") 300 else 200
                            val totalFinal = horasInt * rigsInt * precioHora
                            val randomCode = "SI-0${Random.nextInt(10)}${Random.nextInt(10)}${Random.nextInt(10)}${Random.nextInt(10)}"
                            val reserva = Reserva(nombre, fecha, rigTipo, rigsInt, totalFinal, metodoPago)
                            val db = Firebase.database.reference
                            val id = db.child("reservas").push().key
                            if (id != null) {
                                db.child("reservas").child(id).setValue(reserva)
                                    .addOnSuccessListener {
                                        val encodedNombre = Uri.encode(nombre)
                                        val encodedFecha = Uri.encode(fecha)
                                        val encodedRigTipo = Uri.encode(rigTipo)
                                        val encodedMetodo = Uri.encode(metodoPago)
                                        val encodedCode = Uri.encode(randomCode)
                                        navController.navigate("thankyou/$encodedNombre/$encodedFecha/$encodedRigTipo/$rigs/$totalFinal/$encodedMetodo/$encodedCode") {
                                            popUpTo("reserva") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        mensaje = "❌ Error al guardar"
                                    }
                            }
                        }
                    } else {
                        mensaje = "Completa todos los campos"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Confirmar reserva", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(mensaje, color = Color.White)
        }
    }
}


@Composable
fun SelectDropdown(label: String, selected: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable { expanded = true }
        ) {
            Text(selected, color = Color.White)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FechaPickerInercia(fecha: String, onFechaSeleccionada: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Button(
        onClick = {
            DatePickerDialog(
                context,
                { _: DatePicker, y: Int, m: Int, d: Int ->
                    onFechaSeleccionada("$d/${m + 1}/$y")
                },
                year,
                month,
                day
            ).apply {
                datePicker.minDate = calendar.timeInMillis
                show()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFFFF6F00))
        Spacer(Modifier.width(8.dp))
        Text(
            if (fecha.isEmpty()) "Seleccionar fecha"
            else "Fecha: $fecha",
            color = Color.White
        )
    }
}
