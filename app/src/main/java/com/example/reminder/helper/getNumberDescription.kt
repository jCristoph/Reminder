package com.example.reminder.helper

fun getNumberDescription(number: Double): String? {
    if (number >= 70.0) {
        return "Sprzedawaj $number"
    }
    if (number <= 30.0) {
        return "Kupuj, wartość: $number"
    }
    return null
}