package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.showShortToast(message: String, value: Float? = null) {
    val formattedValue = if (value != null) ": " + String.format("%.2f", value) else ""
    val completeMessage = message + formattedValue
    Toast.makeText(this, completeMessage, Toast.LENGTH_SHORT).show()
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}







