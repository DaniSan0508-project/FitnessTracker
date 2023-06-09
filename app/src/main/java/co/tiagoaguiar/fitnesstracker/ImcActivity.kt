package co.tiagoaguiar.fitnesstracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import co.tiagoaguiar.fitnesstracker.model.App
import co.tiagoaguiar.fitnesstracker.model.Calc

class ImcActivity : AppCompatActivity() {

    private lateinit var editHeight: EditText
    private lateinit var editWeight: EditText

    @SuppressLint("ServiceCast")
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


            val title = getString(R.string.imc_response, result)
            val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(imcResponseId)
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> TODO("Not yet implemented") }
                .setNegativeButton(R.string.save) { dialog, with ->
                    Thread {
                        val app = (application as App)
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = result))

                        runOnUiThread {
                            openListActivity()
                        }
                    }.start()


                }
                .create()
                .show()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.menu_search) {
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
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