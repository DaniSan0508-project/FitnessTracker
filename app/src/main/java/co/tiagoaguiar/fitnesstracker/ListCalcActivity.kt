package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.App
import co.tiagoaguiar.fitnesstracker.model.AppDatabase
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.lang.Error
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)


        val result = mutableListOf<Calc>()
        val adapter = ListCalcAdapter(result)
        rv = findViewById(R.id.list_calc_view)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val type = intent?.extras?.getString("type")
        if (type != null) {
            Thread {
                val app = application as App
                val dao = app.db.calcDao()
                val response = dao.getRegisterByType(type)

                runOnUiThread {
                    result.addAll(response)
                    adapter.notifyDataSetChanged()
                }
            }.start()

        } else {
            throw IllegalStateException("type not found")
        }
    }

    private inner class ListCalcAdapter(
        private val mainItems: List<Calc>,
    ) : RecyclerView.Adapter<ListCalcAdapter.ListCalcItemMViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcItemMViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ListCalcItemMViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListCalcItemMViewHolder, position: Int) {
            val currentItem = mainItems[position]
            holder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class ListCalcItemMViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val tv = itemView as TextView
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                val data = sdf.format(item.createdDate)
                val res = item.res
                tv.text = getString(R.string.list_response, res, data)
            }
        }
    }
}