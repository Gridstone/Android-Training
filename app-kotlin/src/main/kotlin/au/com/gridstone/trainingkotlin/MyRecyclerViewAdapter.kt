package au.com.gridstone.trainingkotlin

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyRecyclerViewAdapter :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

  var tapHandler: ((id: Int) -> Unit)? = null

  private lateinit var myDataset: List<PokemonSummary>

  class MyViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view) {
    val titleTextView = view.findViewById<TextView>(R.id.imageListTitle)
        .apply {
          this.gravity = Gravity.CENTER_VERTICAL
          this.background.mutate()
              .alpha = 200
        }
    val imageView = view.findViewById<ImageView>(R.id.imageListImageView)
  }

  fun set(data: List<PokemonSummary>) {
    myDataset = data
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MyRecyclerViewAdapter.MyViewHolder {
    val row = LayoutInflater.from(parent.context)
        .inflate(R.layout.recyclerview_row, parent, false) as FrameLayout
    return MyViewHolder(row)
  }

  override fun onBindViewHolder(
    holder: MyViewHolder,
    position: Int
  ) {
    val pokemon = myDataset[position]
    // Assume that the pokemon's ID is their position in the array plus one, as the endpoint does not give ID alongside name.
    val id: Int = position + 1
    val displayable = PokemonListDisplayble(pokemon, id)
    holder.titleTextView.text = displayable.title
    Picasso.get()
        .load(displayable.imageURL)
        .into(holder.imageView)

    holder.view.setOnClickListener {
      tapHandler?.invoke(id)
    }
  }

  override fun getItemCount() = myDataset.size
}