package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import co.tiagoaguiar.fitnesstracker.model.Calc

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
                .setNegativeButton(R.string.save) { dialog, _ ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = result))
                        runOnUiThread {
                            startListCalcActivity("imc")
                        }

                    }.start()
                }
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            currentFocus?.hideKeyboard()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                finish()
                startListCalcActivity("imc")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startListCalcActivity(type: String) {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
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

    private fun imcCal(weightValue: String, heightValue: String): Double {
        val weightFloat = weightValue.toDouble()
        val heightFloat = heightValue.toDouble()
        val heightInMeters = heightFloat / 100

        return weightFloat / (heightInMeters * heightInMeters)
    }

    @StringRes
    private fun imcResponse(imcValue: Double): Int {
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