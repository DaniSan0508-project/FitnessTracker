package co.tiagoaguiar.fitnesstracker

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.addAll(
            listOf(
                MainItem(1, R.drawable.ic_baseline_wb_sunny_24, R.string.app_name, Color.BLUE),
                MainItem(2, R.drawable.ic_baseline_wb_sunny_24, R.string.result_imc, Color.GREEN),
                MainItem(3, R.drawable.ic_baseline_wb_sunny_24, R.string.app_name, Color.GRAY),
                MainItem(4, R.drawable.ic_baseline_wb_sunny_24, R.string.result_imc, Color.CYAN)
            )
        )

        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = MainAdapter(mainItems, this)
        rvMain.layoutManager = GridLayoutManager(this, 2)

    }

    override fun onClick(id: Int) {
        when(id){
            1->{
                val intent = Intent(this,ImcActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private inner class MainAdapter(
        private val mainItems: MutableList<MainItem>,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: MainItem) {
                val buttonText = itemView.findViewById<TextView>(R.id.item_label)
                val img = itemView.findViewById<ImageView>(R.id.item_img)
                val container = itemView.findViewById<LinearLayout>(R.id.item_container)
                buttonText.setText(item.textStringId)
                img.setImageResource(item.drawableId)
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    onItemClickListener.onClick(item.id)
                }
            }


        }
    }
}