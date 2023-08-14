package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.widget.Toast

fun Context.showShortToast(message: String, value: Float? = null) {
    val formattedValue = if (value != null) String.format("%.2f", value) else ""
    val completeMessage = message + formattedValue
    Toast.makeText(this, completeMessage, Toast.LENGTH_SHORT).show()
}



