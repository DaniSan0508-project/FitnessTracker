package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.lang.IllegalStateException
import java.text.SimpleDateFormat

class ListCalcActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView
    private var items: List<Calc> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        rvMain = findViewById(R.id.rv_items)
        rvMain.layoutManager = LinearLayoutManager(this)

        val type = intent?.extras?.getString("type")

        if (type != null) {
            Thread {
                val app = application as App
                val dao = app.db.calcDao()
                items = dao.findByType(type)

                runOnUiThread {
                    rvMain.adapter = MainAdapter(items)
                }

            }.start()
        } else {
            throw IllegalStateException("Type not found")
        }
    }

    private inner class MainAdapter(private val items: List<Calc>) : RecyclerView.Adapter<MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int = items.size
    }

    private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(item: Calc) {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = dateFormatter.format(item.createdDate)
            val text = "${item.type} : ${String.format("%.2f",item.res) } : Data: $formattedDate"
            textView.text = text
        }
    }
}
