package hr.alenmagdic.carperformancesimulator.app.ui

import android.widget.EditText

fun EditText.getTextAsString() = text.toString()
fun EditText.getTextAsDouble() = text.toString().toDoubleOrNull()
fun EditText.getTextAsInt() = text.toString().toIntOrNull()
fun EditText.getTextAsLong() = text.toString().toLongOrNull()

fun Number?.toStringOrEmpty() = this?.toString() ?: ""