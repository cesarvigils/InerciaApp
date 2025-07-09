package com.example.inercia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.navigation.NavController

@Composable
fun ThankYouScreen(
    nombre: String,
    fecha: String,
    rigTipo: String,
    rigs: String,
    total: String,
    metodo: String,
    codigo: String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Reserva confirmada!", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Text("Detalles de la reserva:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))

        InfoItem("Nombre", nombre)
        InfoItem("Fecha", fecha)
        InfoItem("Tipo de Rig", rigTipo)
        InfoItem("Cantidad de Rigs", rigs)
        InfoItem("Método de pago", metodo)
        InfoItem("Total", "L. $total")
        InfoItem("Código de reserva", codigo)

        Spacer(modifier = Modifier.height(30.dp))
        Text("Gracias por reservar con Inercia 🏁", color = Color(0xFFFF6F00), fontSize = 16.sp)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
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
