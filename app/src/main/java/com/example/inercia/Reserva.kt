package com.example.inercia.models

data class Reserva(
    val nombre: String = "",
    val fecha: String = "",
    val rigTipo: String = "",
    val cantidad: Int = 0,
    val precioTotal: Int = 0,
    val metodoPago: String = ""
)

