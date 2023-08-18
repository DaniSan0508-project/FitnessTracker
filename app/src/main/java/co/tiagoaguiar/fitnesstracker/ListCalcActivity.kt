package co.tiagoaguiar.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.lang.IllegalStateException
import java.text.SimpleDateFormat

class ListCalcActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView
    private var items: MutableList<Calc> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val adapter =  MainAdapter(items)
        rvMain = findViewById(R.id.rv_items)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter


        val type = intent?.extras?.getString("type")

        if (type != null) {
            Thread {
                val app = application as App
                val dao = app.db.calcDao()
                val newItems = dao.findByType(type)

                runOnUiThread {
                    items.clear()
                    items.addAll(newItems)
                    adapter.notifyDataSetChanged()
                }

            }.start()

        } else {
            throw IllegalStateException("Type not found")
        }
    }

    private inner class MainAdapter(private val items: MutableList<Calc>) : RecyclerView.Adapter<MainViewHolder>() {

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


        init {
            itemView.setOnLongClickListener {
                showDialogToDeleteItem(adapterPosition)
                true
            }
        }

        fun bind(item: Calc) {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = dateFormatter.format(item.createdDate)
            val text = "${item.type} : ${String.format("%.2f",item.res) } : Data: $formattedDate"
            textView.text = text
        }
    }
    private fun showDialogToDeleteItem(position: Int) {
        val item = items[position]

        AlertDialog.Builder(this)
            .setTitle("Confirmação")
            .setMessage("Você deseja deletar este item?")
            .setPositiveButton("Sim") { _, _ ->
               deleteItem(item, position)
            }
            .setNegativeButton("Não", null)
            .show()
    }
    private fun deleteItem(item: Calc, position: Int) {
        Thread {
            val app = application as App
            val dao = app.db.calcDao()

            dao.delete(item)

            runOnUiThread {
                items.removeAt(position)
                rvMain.adapter?.notifyItemRemoved(position)
            }
        }.start()
    }



}
