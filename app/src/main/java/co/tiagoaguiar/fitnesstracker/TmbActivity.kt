package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import co.tiagoaguiar.fitnesstracker.model.Calc

class TmbActivity : AppCompatActivity() {
    private lateinit var lifestyle: AutoCompleteTextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var weight: EditText
    private lateinit var height: EditText
    private lateinit var age: EditText
    private lateinit var btnTmbCalc: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)


        weight = findViewById(R.id.tmb_weight_value)
        height = findViewById(R.id.tmb_height_value)
        age = findViewById(R.id.tmb_age_value)
        lifestyle = findViewById(R.id.tmb_auto_lifestyle)
        radioGroup = findViewById(R.id.tmb_gender)
        btnTmbCalc = findViewById(R.id.btn_tmb_calc)

        val items = resources.getStringArray(R.array.tmb_lifestyle)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifestyle.setAdapter(adapter)
        lifestyle.setText(items.first(), false)


        btnTmbCalc.setOnClickListener {
            val result = calculateTMB()
            if(result != false){
                AlertDialog.Builder(this)
                    .setTitle(R.string.tmb_alert_title)
                    .setMessage("Sua TMB é:" + String.format("%.2f", result))
                    .setNegativeButton(R.string.save) { dialog, _ ->
                        Thread {
                            try {
                                val app = application as App
                                val dao = app.db.calcDao()

                                dao.insert(Calc(type = "tmb", res = result as Double))

                                runOnUiThread {
                                    startListCalcActivity("tmb")
                                }
                            } catch (e: Exception) {
                                Log.e("DB_ERROR", "Erro ao salvar no BD", e)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                finish()
                startListCalcActivity("tmb")
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

    fun calculateTMB(): Any? {
        val userWeight = weight.text.toString().toDoubleOrNull()
        val userHeight = height.text.toString().toDoubleOrNull()
        val userAge = age.text.toString().toIntOrNull()

        if (userWeight == null || userHeight == null || userAge == null) {
            Toast.makeText(
                this,
                "Por favor, preencha todos os campos corretamente.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val selectedLifeStyle = lifestyle.text.toString()
        val selectedGenderId = radioGroup.checkedRadioButtonId

        val tmb = if (selectedGenderId == R.id.radio_male) {
            88.362 + (13.397 * userWeight) + (4.799 * userHeight) - (5.677 * userAge)
        } else {
            447.593 + (9.247 * userWeight) + (3.098 * userHeight) - (4.330 * userAge)
        }

        val factor = when (selectedLifeStyle) {
            "Pouco ou nenhum exercício" -> 1.2
            "Exercício leve, 1 a 3 dias por semana" -> 1.375
            "Exercício moderado, 3 a 5 dias por semana" -> 1.55
            "Exercício pesado, 6 a 7 dias por semana" -> 1.725
            "Exercício pesado diariamente e até 2x por dia" -> 1.9
            else -> 1.0
        }

        return tmb * factor
    }
}