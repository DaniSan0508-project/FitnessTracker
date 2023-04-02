package co.tiagoaguiar.fitnesstracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onViewClick(id: Int)
}

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var rvMain: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_wb_sunny_24,
                textString = R.string.label_imc,
                color = Color.GREEN
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.ic_tmb,
                textString = R.string.label_tmb,
                color = Color.YELLOW
            )
        )

        mainItems.add(
            MainItem(
                id = 3,
                drawableId = R.drawable.ic_tmb,
                textString = R.string.label_tmb,
                color = Color.YELLOW
            )
        )

        mainItems.add(
            MainItem(
                id = 4,
                drawableId = R.drawable.ic_tmb,
                textString = R.string.label_tmb,
                color = Color.YELLOW
            )
        )

        var adapter = MainAdapter(mainItems, this)
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onViewClick(id: Int) {
        Toast.makeText(this, "Clicou $id", Toast.LENGTH_SHORT).show()
    }

    private inner class MainAdapter(
        private val mainItems: MutableList<MainItem>,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val currentItem = mainItems[position]
            holder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawableId)
                name.setText(item.textString)
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    onItemClickListener.onViewClick(item.id)
                }
            }
        }
    }

}