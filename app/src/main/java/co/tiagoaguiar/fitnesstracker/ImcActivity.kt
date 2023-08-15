package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

class ImcActivity : AppCompatActivity() {
    private lateinit var weight: EditText
    private lateinit var height: EditText
    private lateinit var btnCalcImc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        weight = findViewById(R.id.imc_weight_value)
        height = findViewById(R.id.imc_height_value)
        btnCalcImc = findViewById(R.id.btn_imc_calc)


        btnCalcImc.setOnClickListener {
            val weightValue = weight.text.toString().trim()
            val heightValue = height.text.toString().trim()

            if (!validateInputs(weightValue, heightValue)) {
                return@setOnClickListener
            }
            val result = imcCal(weightValue, heightValue)
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_imc_dialog))
                .setMessage(getString(imcResponse(result)) + " : " + String.format("%.2f", result))
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            currentFocus?.hideKeyboard()
        }


    }

    private fun validateInputs(weightValue: String, heightValue: String): Boolean {
        if (weightValue.isEmpty() ||
            weightValue.startsWith("0") ||
            heightValue.isEmpty() ||
            heightValue.startsWith("0")
        ) {
            Toast.makeText(
                this,
                "Por favor, insira valores válidos para peso e altura.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val weightFloat = weightValue.toFloatOrNull()
        val heightFloat = heightValue.toFloatOrNull()

        if (weightFloat == null || heightFloat == null || weightFloat == 0f || heightFloat == 0f) {
            Toast.makeText(this, "Peso e altura devem ser diferentes de 0.", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true

    }

    private fun imcCal(weightValue: String, heightValue: String): Float {
        val weightFloat = weightValue.toFloat()
        val heightFloat = heightValue.toFloat()
        val heightInMeters = heightFloat / 100

        return weightFloat / (heightInMeters * heightInMeters)
    }

    @StringRes
    private fun imcResponse(imcValue: Float): Int {
        return when {
            imcValue <= 15 -> R.string.imc_severely_low_weight
            imcValue <= 16 -> R.string.imc_very_low_weight
            imcValue <= 18.5 -> R.string.imc_low_weight
            imcValue <= 24.9 -> R.string.normal
            imcValue <= 29.9 -> R.string.imc_high_weight
            imcValue <= 34.9 -> R.string.imc_so_high_weight
            imcValue <= 39.9 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

}