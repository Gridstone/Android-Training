package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import au.com.gridstone.robocop.utils.bindView
import com.jakewharton.rxbinding3.view.clicks

class MyRecyclerViewAdapter :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

  private var tapRelay: PublishRelay<Int> = PublishRelay.create()
  val selections: Observable<Int> = tapRelay

  private lateinit var myDataset: List<PokemonSummary>

  fun set(data: List<PokemonSummary>) {
    myDataset = data
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MyViewHolder {
    val row = LayoutInflater.from(parent.context)
        .inflate(R.layout.recyclerview_row, parent, false) as FrameLayout
    return MyViewHolder(row)
  }

  override fun onBindViewHolder(
    holder: MyViewHolder,
    position: Int
  ) {
    val pokemon = myDataset[position]
    val id = position + 1
    // Assume that the pokemon's ID is their position in the array plus one, as the endpoint does not give ID alongside name.
    val displayable = PokemonDisplayable(pokemon.name, id)
    holder.bindTo(displayable, id)
  }

  override fun getItemCount() = myDataset.size

  inner class MyViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view) {
    private val titleTextView: TextView by bindView(R.id.imageListTitle)
    private val imageView: ImageView by bindView(R.id.imageListImageView)

    private var id: Int? = null

    init {
      view.clicks().map { id }.subscribe(tapRelay)
    }

    fun bindTo(displayable: PokemonDisplayable, id: Int) {
      this.id = id
      titleTextView.text = displayable.title
      Picasso.get()
          .load(displayable.imageURL)
          .into(imageView)
    }
  }
}