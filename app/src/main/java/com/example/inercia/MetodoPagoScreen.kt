package com.example.inercia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetodoPagoScreen(onSeleccionar: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecciona un método de pago",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MetodoPagoButton("Transferencia bancaria") {
            onSeleccionar(
                "Transferencia\nBanco Ficohsa: 00123456789\nBanco Atlántida: 00987654321\nBAC: 00345678900"
            )
        }

        MetodoPagoButton("Pago en efectivo (local)") {
            onSeleccionar("Efectivo en local")
        }

        MetodoPagoButton("Pago con tarjeta (local)") {
            onSeleccionar("Tarjeta en local")
        }
    }
}

@Composable
fun MetodoPagoButton(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = texto, color = Color.White, fontSize = 16.sp)
    }
}
