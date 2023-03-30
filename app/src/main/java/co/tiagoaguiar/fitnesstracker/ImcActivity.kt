package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes

class ImcActivity : AppCompatActivity() {

    private lateinit var editHeight: EditText
    private lateinit var editWeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        editHeight = findViewById(R.id.edit_imc_height)
        editWeight = findViewById(R.id.edit_imc_weight)

        val btnImcSend: Button = findViewById(R.id.btn_imc_send)

        btnImcSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.valid_imc_fields, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val height = editHeight.text.toString().toInt()
            val weight = editWeight.text.toString().toInt()

            val result = calculateImc(weight, height)
            val imcResponseId = imcResponse(result)

            Toast.makeText(this, imcResponseId, Toast.LENGTH_LONG).show()
        }
    }

    fun validate(): Boolean {
        return (
                editHeight.text.toString().isNotEmpty()
                        && editWeight.text.toString().isNotEmpty()
                        && !editHeight.text.toString().startsWith("0")
                        && !editWeight.text.toString().startsWith("0")
                )
    }

    private fun calculateImc(weight: Int, height: Int): Double {
        return weight / ((height / 100.0) * (height / 100.0))
    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
        return when {
            imc < 15.0 -> R.string.imc_severaly_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.normal
            imc < 30.0 -> R.string.imc_hight_weight
            imc < 35.0 -> R.string.imc_so_hight_weight
            imc < 40.0 -> R.string.imc_severely_hight_weight
            else -> R.string.imc_extreme_weight
        }
    }
}