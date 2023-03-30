package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
}