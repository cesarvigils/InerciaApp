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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Detalles de la reserva:",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        ReservaItem("Nombre", nombre)
        ReservaItem("Fecha", fecha)
        ReservaItem("Tipo de Rig", rigTipo)
        ReservaItem("Cantidad de Rigs", rigs)
        ReservaItem("Método de pago", metodo)
        ReservaItem("Total", "L. $total")
        ReservaItem("Código de reserva", codigo)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Gracias por reservar con Inercia 🏁",
            color = Color(0xFFFF6F00),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ReservaItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.DarkGray, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp)
    }
}
